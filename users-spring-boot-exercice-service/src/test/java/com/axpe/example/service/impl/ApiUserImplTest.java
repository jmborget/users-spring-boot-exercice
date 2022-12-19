package com.axpe.example.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.axpe.example.persistence.entities.User;
import com.axpe.example.persistence.repository.UsersRepository;
import com.axpe.example.service.ApiEmails;
import com.axpe.example.service.dto.ParamsDTO;
import com.axpe.example.service.dto.UserDTO;
import com.axpe.example.service.dto.ValidationDTO;
import com.axpe.example.service.exceptions.BadRequestExcepcion;
import com.axpe.example.service.exceptions.ConflictExcepcion;
import com.axpe.example.service.exceptions.ContentNotFoundException;
import com.axpe.example.service.exceptions.ErrorCode;
import com.axpe.example.service.utils.PBKDF2;
import com.axpe.example.service.utils.UserMaper;

@Service
@ExtendWith(MockitoExtension.class)
class ApiUserImplTest {
    
    @Mock
    private UsersRepository userRepository;
    
    @Mock
    private UserMaper userMaper;
    
    @Spy
    private PBKDF2 pbkdf2;
    
    @Mock
    private ApiEmails apiEmail;
    
    @InjectMocks
    private ApiUserImpl apiUserService;
    
    private User user = new User();
    private UserDTO userDTO = new UserDTO();
    private User user2 = new User();
    private User user3 = new User();
    
    //    private CreateValidateEmailDTO createValidateEmailDTO = new CreateValidateEmailDTO(
    //            "jose@axpe.com");
    private UserDTO createUserDTO = new UserDTO();
    
    @BeforeEach
    public void init() {
        
        user.setId(1L);
        user.setEmail("jborgex@gmail.com");
        user.setFirstName("Jose");
        user.setLastName("Borge");
        user.setLastName2("Torres");
        user.setNickname("josborg");
        user.setPassword("1234".hashCode() + "");
        user.setNif("123456789N");
        user.setPhone("+34683317517");
        user.setStatusUser("ACTIVO");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCanceledAt(LocalDateTime.now());
        
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setLastName2(user.getLastName2());
        userDTO.setLastName(user.getLastName());
        userDTO.setNickname(user.getNickname());
        userDTO.setNif(user.getNif());
        userDTO.setPhone(user.getPhone());
        userDTO.setStatusUser(user.getStatusUser());
        userDTO.setCreatedAt(user.getCreatedAt().toString());
        userDTO.setUpdatedAt(user.getUpdatedAt().toString());
        userDTO.setCanceledAt(user.getCanceledAt().toString());
        
        user2 = user.withId(2L);
        user3 = user.withId(3L);
    }
    
    @Test
    void getUserByIdTest() {
        //given
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMaper.userEntitytoUserDTO(user)).thenReturn(userDTO);
        UserDTO userDTO = apiUserService.getUserById(1L);
        
        //then
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
    }
    
    @Test
    void getUserByIdNotFoundTest() {
        //given
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ContentNotFoundException exception = assertThrows(ContentNotFoundException.class, () -> {
            apiUserService.getUserById(1L);
        });
        
        //then
        Assertions.assertEquals(ErrorCode.OBJECT_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        
    }
    
    //    
    //    // when
    //    ContentNotFoundException exception = assertThrows(ContentNotFoundException.class, () -> {
    //        this.userServiceImpl.findById(1L);
    //    });
    //
    //    // then
    //    assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.OBJECT_NOT_FOUND);
    //    verify(mockUserRepository, times(1)).findById(Mockito.anyLong());
    //    
    @Test
    void isPasswordUserTest() {
        //given
        user.setPassword(this.pbkdf2.generate("1234").get());
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        boolean valid = apiUserService.isPasswordUser(1L, "1234");
        
        //then
        Assertions.assertTrue(valid);
        
    }
    
    @Test
    void isNotPasswordUserTest() {
        //given
        user.setPassword(this.pbkdf2.generate("1235").get());
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        //then
        Assertions.assertFalse(apiUserService.isPasswordUser(1L, "1234"));
        
    }
    
    @Test
    void isPasswordUserNotFoundTest() {
        //given
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ContentNotFoundException exception = assertThrows(ContentNotFoundException.class, () -> {
            apiUserService.isPasswordUser(1L, "1234");
        });
        
        //then
        Assertions.assertEquals(ErrorCode.OBJECT_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        
    }
    
    @Test
    void deleteByIdTest() {
        //when
        Mockito.when(this.userRepository.updateStatusDelete(Mockito.anyLong(), Mockito.any()))
                .thenReturn(1);
        //then
        Assertions.assertDoesNotThrow(() -> {
            apiUserService.delete(1L);
        });
        
    }
    
    @Test
    void deleteByIdNotFoundTest() {
        //given
        
        //when
        Mockito.when(this.userRepository.updateStatusDelete(Mockito.anyLong(), Mockito.any()))
                .thenReturn(0);
        ContentNotFoundException exception = assertThrows(ContentNotFoundException.class, () -> {
            apiUserService.delete(1L);
        });
        
        //then
        Assertions.assertEquals(ErrorCode.OBJECT_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).updateStatusDelete(Mockito.anyLong(), Mockito.any());
        
    }
    
    @Test
    void editByIdTest() {
        //given
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userMaper.userDTOtoUserEntity(createUserDTO)).thenReturn(user);
        apiUserService.edit(1L, createUserDTO);
        
        //then
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void editByIdNotFoundTest() {
        //given
        
        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        ContentNotFoundException exception = assertThrows(ContentNotFoundException.class, () -> {
            apiUserService.edit(1L, userDTO);
        });
        
        //then
        Assertions.assertEquals(ErrorCode.OBJECT_NOT_FOUND, exception.getErrorCode());
        verify(userRepository, times(1)).findById(Mockito.anyLong());
        
    }
    
    @Test
    void editByIdConflictTest() {
        
        UserDTO createUserDTO = new UserDTO();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(false);
        
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.edit(1L, createUserDTO);
        });
        
    }
    
    @Test
    void saveUserTest() {
        
        Mockito.when(userRepository.isSaveable(Mockito.any())).thenReturn(true);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(user);
        Mockito.when(userMaper.userDTOtoUserEntity(createUserDTO)).thenReturn(user);
        Mockito.when(userMaper.userEntitytoUserDTO(user)).thenReturn(userDTO);
        Mockito.when(apiEmail.validate(Mockito.any())).thenReturn(new ValidationDTO(true));
        
        UserDTO userDTO = apiUserService.save(createUserDTO);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void saveByIdConflictTest() {
        
        Mockito.when(userRepository.isSaveable(Mockito.any())).thenReturn(false);
        Mockito.when(apiEmail.validate(Mockito.any())).thenReturn(new ValidationDTO(true));
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.save(createUserDTO);
        });
        
    }
    
    @Test
    void saveByIdConflictInvalidTest() {
        
        Mockito.when(apiEmail.validate(Mockito.any())).thenReturn(new ValidationDTO(false));
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.save(createUserDTO);
        });
        
    }
    
    @Test
    void patchPhoneByIdTest() {
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("phone", "645345323");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        userDTO.setPhone("645345323");
        user.setPhone("645345323");
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        
        UserDTO userDTO = apiUserService.patchUserById(1L, map);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void patchEmailByIdTest() {
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("email", "jose@axpe.com");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        userDTO.setEmail("jose@axpe.com");
        user.setEmail("jose@axpe.com");
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        
        UserDTO userDTO = apiUserService.patchUserById(1L, map);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void patchNifByIdTest() {
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("nif", "123465432J");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        userDTO.setNif("123465432J");
        user.setNif("123465432J");
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        
        UserDTO userDTO = apiUserService.patchUserById(1L, map);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void patchNicknameByIdTest() {
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("nickname", "jborge");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        userDTO.setNickname("jborge");
        user.setNickname("jborge");
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        
        UserDTO userDTO = apiUserService.patchUserById(1L, map);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void patchPasswordByIdTest() {
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("password", "123456789");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(true);
        userDTO.setPassword("123456789");
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        
        UserDTO userDTO = apiUserService.patchUserById(1L, map);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(user.getCreatedAt().toString(), userDTO.getCreatedAt());
        }, () -> {
            Assertions.assertEquals(user.getId(), userDTO.getId());
        }, () -> {
            Assertions.assertEquals(user.getEmail(), userDTO.getEmail());
        }, () -> {
            Assertions.assertEquals(user.getFirstName(), userDTO.getFirstName());
        }, () -> {
            Assertions.assertEquals(user.getLastName(), userDTO.getLastName());
        }, () -> {
            Assertions.assertEquals(user.getLastName2(), userDTO.getLastName2());
        }, () -> {
            Assertions.assertEquals(user.getNickname(), userDTO.getNickname());
        }, () -> {
            Assertions.assertEquals(user.getNif(), userDTO.getNif());
        }, () -> {
            Assertions.assertEquals(user.getPhone(), userDTO.getPhone());
        }, () -> {
            Assertions.assertEquals(user.getStatusUser(), userDTO.getStatusUser());
        });
        
    }
    
    @Test
    void patchByIdNotFoundTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(null));
        Map<Object, Object> map = new HashMap<Object, Object>();
        Assertions.assertThrows(ContentNotFoundException.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void patchByIdConflictBadParamTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("nickname2", "nickname");
        
        Assertions.assertThrows(BadRequestExcepcion.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void patchByIdConflictPhoneTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(false);
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("phone", user.getPhone());
        
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void patchByIdConflictEmailTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(false);
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("email", user.getEmail());
        
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void patchByIdConflictNifTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(false);
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("nif", user.getNif());
        
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void patchByIdConflictNicknameTest() {
        
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userMaper.userEntitytoUserDTO(Mockito.any())).thenReturn(userDTO);
        Mockito.when(userRepository.isEditable(Mockito.anyLong(), Mockito.any())).thenReturn(false);
        
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        map.put("nickname", user.getNickname());
        
        Assertions.assertThrows(ConflictExcepcion.class, () -> {
            apiUserService.patchUserById(1L, map);
        });
        
    }
    
    @Test
    void findByParamsTest() {
        PageImpl<User> page = new PageImpl<User>(Arrays.asList(user, user2, user3));
        Mockito.when(userRepository.findByParams(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.any())).thenReturn(page);
        
        ParamsDTO params = ParamsDTO.builder().firstName("Jose").lastName("Borge")
                .lastName2("Torres").email("jborgex@gmail.com").nif("123456789N")
                .phone("+34683317517").nickname("josborg").statusUser("ACTIVO")
                .sortBy("firstName, lastName, lastName2, email, nif, phone, nickname, statusUser")
                .pageable(PageRequest.of(0, 10)).build();
        
        Page<UserDTO> pageResult = apiUserService.findByParams(params);
        
        Assertions.assertEquals(pageResult.getSize(), page.getSize());
        
    }
    
}
