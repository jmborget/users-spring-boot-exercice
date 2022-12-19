package com.axpe.example.presentation.controllers.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.axpe.example.presentation.controllers.UsersController;
import com.axpe.example.presentation.http.CustomHeaders;
import com.axpe.example.service.ApiUsers;
import com.axpe.example.service.dto.ParamsDTO;
import com.axpe.example.service.dto.UserDTO;
import com.github.fge.jsonpatch.JsonPatch;

import lombok.RequiredArgsConstructor;

/**
 * {@inheritDoc}
 */
//@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsersControllerImpl implements UsersController {
    
    private final ApiUsers apiUser;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = { BASE_URL })
    public ResponseEntity<Void> newUser(@RequestBody final UserDTO newUser,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId) {
        
        UserDTO user = this.apiUser.save(newUser);
        HttpHeaders headers = this.buildXRequestHeader(xRequestId);
        headers.add(CustomHeaders.LOCATION, UsersControllerImpl.BASE_URL + "/" + user.getId());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    // @formatter:off
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = { BASE_URL })
    public ResponseEntity<Page<UserDTO>> listAllUsers(
            @RequestParam(name = "firstName", required = false) final String firstName,
            @RequestParam(name = "lastName", required = false) final String lastName,
            @RequestParam(name = "lastName2", required = false) final String lastName2,
            @RequestParam(name = "email", required = false) final String email,
            @RequestParam(name = "nif", required = false) final String nif,
            @RequestParam(name = "phone", required = false) final String phone,
            @RequestParam(name = "nickname", required = false) final String nickname,
            @RequestParam(name = "statusUser", required = false) final String statusUser,
            @PageableDefault(sort = { "id" }, direction = Direction.ASC,
            size = SIZE, page = PAGE) final Pageable pageable,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId){
    // @formatter:on 
        
        ParamsDTO params = ParamsDTO.builder().firstName(firstName).lastName(lastName)
                .lastName2(lastName2).email(email).nif(nif).phone(phone).nickname(nickname)
                .statusUser(statusUser).pageable(pageable).build();
        
        return new ResponseEntity<>(this.apiUser.findByParams(params),
                this.buildXRequestHeader(xRequestId), HttpStatus.OK);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = { BASE_URL + URL_ID })
    public ResponseEntity<UserDTO> getUserById(@PathVariable(PATH_VARIABLE_NAME) final Long id,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId) {
        
        return new ResponseEntity<>(this.apiUser.getUserById(id),
                this.buildXRequestHeader(xRequestId), HttpStatus.OK);
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = { BASE_URL + URL_ID })
    public ResponseEntity<Void> putUserById(@PathVariable(PATH_VARIABLE_NAME) Long id,
            @RequestBody UserDTO editUser,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) String xRequestId) {
        
        this.apiUser.edit(id, editUser);
        
        return new ResponseEntity<>(this.buildXRequestHeader(xRequestId), HttpStatus.NO_CONTENT);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @DeleteMapping(value = { BASE_URL + URL_ID })
    public ResponseEntity<Void> deleteUserById(@PathVariable(PATH_VARIABLE_NAME) final Long id,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId) {
        
        this.apiUser.delete(id);
        
        return new ResponseEntity<>(this.buildXRequestHeader(xRequestId), HttpStatus.NO_CONTENT);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, value = {
            BASE_URL + URL_ID })
    public ResponseEntity<UserDTO> patchUserById(@PathVariable(PATH_VARIABLE_NAME) final Long id,
            @RequestBody final Map<Object, Object> fields,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId) {
        
        return new ResponseEntity<>(this.apiUser.patchUserById(id, fields),
                this.buildXRequestHeader(xRequestId), HttpStatus.OK);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @PatchMapping(consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE, value = {
            BASE_URL + URL_ID })
    public ResponseEntity<UserDTO> updateUserById(@PathVariable(PATH_VARIABLE_NAME) final Long id,
            @RequestBody final JsonPatch patchUser,
            @RequestHeader(value = CustomHeaders.X_REQUEST_ID, required = false) final String xRequestId) {
        
        return new ResponseEntity<>(this.apiUser.updateUserById(id, patchUser),
                this.buildXRequestHeader(xRequestId), HttpStatus.OK);
    }
    
    //Construye la cabecera de XRequestID a partir de una cadena de caracteres que puede ser nula, 
    //en ese caso construye una cabecera vacía.
    private HttpHeaders buildXRequestHeader(final String xRequestId) {
        HttpHeaders headers = new HttpHeaders();
        return Optional.ofNullable(xRequestId).map(v -> {
            headers.add(CustomHeaders.X_REQUEST_ID, v);
            return headers;
        }).orElse(headers);
        //HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.setLocation(WebMvcLinkBuilder
        //              .linkTo(WebMvcLinkBuilder.methodOn(UserControllerImpl.class).findById(locale, requestId, user.getId()))
        //              .withSelfRel().getTemplate().expand());
        //
        //return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).build();
    }
}
