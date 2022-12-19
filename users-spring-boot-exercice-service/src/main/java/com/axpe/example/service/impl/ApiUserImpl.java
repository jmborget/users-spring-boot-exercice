package com.axpe.example.service.impl;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.axpe.example.persistence.entities.User;
import com.axpe.example.persistence.repository.UsersRepository;
import com.axpe.example.service.ApiEmails;
import com.axpe.example.service.ApiUsers;
import com.axpe.example.service.dto.CreateValidateEmailDTO;
import com.axpe.example.service.dto.ParamsDTO;
import com.axpe.example.service.dto.UserDTO;
import com.axpe.example.service.exceptions.BadRequestExcepcion;
import com.axpe.example.service.exceptions.ConflictExcepcion;
import com.axpe.example.service.exceptions.ContentNotFoundException;
import com.axpe.example.service.exceptions.DevelopmentException;
import com.axpe.example.service.exceptions.ErrorCode;
import com.axpe.example.service.utils.PBKDF2;
import com.axpe.example.service.utils.UserMaper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;

import lombok.RequiredArgsConstructor;

/**
 * {@inheritDoc}
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiUserImpl implements ApiUsers {
    
    private final UsersRepository userRepository;
    
    private final UserMaper userMaper;
    
    private final PBKDF2 pbkdf2;
    
    private final ApiEmails apiEmails;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Page<UserDTO> findByParams(final ParamsDTO params) {
        
        return this.userRepository
                .findByParams(params.getFirstName(), params.getLastName(), params.getLastName2(),
                        params.getEmail(), params.getPhone(), params.getNif(), params.getNickname(),
                        params.getStatusUser(), params.getPageable())
                .map(this.userMaper::userEntitytoUserDTO);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO patchUserById(final Long id, final Map<Object, Object> fields) {
        
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        
        UserDTO userDTO = this.userMaper.userEntitytoUserDTO(user);
        
        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(UserDTO.class, (String) key);
            this.setField(field, userDTO, value);
        });
        
        user = this.userMaper.userDTOtoUserEntity(userDTO);
        
        if (!this.userRepository.isEditable(id, user)) {
            throw new ConflictExcepcion(ErrorCode.INVALID_VALUE);
        }
        
        return this.userMaper.userEntitytoUserDTO(this.userRepository.save(user));
    }
    
    private void setField(Field field, UserDTO userDTO, Object value) {
        if (field == null) {
            throw new BadRequestExcepcion(ErrorCode.INVALID_VALUE);
        }
        if (field.toString().equals("password")) {
            value = this.pbkdf2.generate(value.toString())
                    .orElseThrow(() -> new DevelopmentException(ErrorCode.GENERIC_INFRASTRUCTURE));
        }
        field.setAccessible(true);
        ReflectionUtils.setField(field, userDTO, value);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final Long id) {
        int status = this.userRepository.updateStatusDelete(id, LocalDateTime.now());
        if (status == 0) {
            throw new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void edit(final Long id, UserDTO createUserDTO) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        
        User userEdit = this.userMaper.userDTOtoUserEntity(createUserDTO);
        if (!this.userRepository.isEditable(id, userEdit)) {
            throw new ConflictExcepcion(ErrorCode.INVALID_VALUE);
        }
        
        String codePassword = this.pbkdf2.generate(userEdit.getPassword())
                .orElseThrow(() -> new DevelopmentException(ErrorCode.GENERIC_INFRASTRUCTURE));
        userEdit.setId(id);
        userEdit.setPassword(codePassword);
        userEdit.setCreatedAt(user.getCreatedAt());
        userEdit.setCanceledAt(user.getCanceledAt());
        this.userRepository.save(userEdit);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO save(final UserDTO userDTO) {
        
        if (!this.apiEmails.validate(new CreateValidateEmailDTO(userDTO.getEmail())).isValid()) {
            throw new ConflictExcepcion(ErrorCode.INVALID_VALUE);
        }
        
        User user = this.userMaper.userDTOtoUserEntity(userDTO);
        
        if (!this.userRepository.isSaveable(user)) {
            throw new ConflictExcepcion(ErrorCode.INVALID_VALUE);
        }
        String codePassword = this.pbkdf2.generate(user.getPassword())
                .orElseThrow(() -> new DevelopmentException(ErrorCode.GENERIC_INFRASTRUCTURE));
        user.setPassword(codePassword);
        
        return this.userMaper.userEntitytoUserDTO(this.userRepository.save(user));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO getUserById(final Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        return this.userMaper.userEntitytoUserDTO(user);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPasswordUser(final Long id, final String pass) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        
        return this.pbkdf2.validate(pass, user.getPassword());
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO updateUserById(final Long id, final JsonPatch patch) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ContentNotFoundException(ErrorCode.OBJECT_NOT_FOUND));
        
        try {
            User userPatched = applyPatchToUser(patch, user);
            user = this.userRepository.save(userPatched);
            
        } catch (JsonProcessingException | JsonPatchException e) {
            throw new DevelopmentException(ErrorCode.GENERIC_INFRASTRUCTURE, e);
        }
        
        return this.userMaper.userEntitytoUserDTO(user);
    }
    
    private User applyPatchToUser(final JsonPatch patch, final User targetCustomer)
            throws JsonPatchException, JsonProcessingException {
        
        ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }
}
