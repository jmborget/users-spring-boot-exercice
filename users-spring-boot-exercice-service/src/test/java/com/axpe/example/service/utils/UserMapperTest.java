package com.axpe.example.service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import com.axpe.example.persistence.entities.User;
import com.axpe.example.service.dto.UserDTO;

@ExtendWith(MockitoExtension.class)
@Service
class UserMapperTest {
    
    @InjectMocks
    UserMaperImpl userMaper;
    
    User user = new User();
    UserDTO userDTO = new UserDTO();
    User user2 = new User();
    UserDTO user2DTO = new UserDTO();
    User user3 = new User();
    UserDTO user3DTO = new UserDTO();
    
    UserDTO createUserDTO = new UserDTO();
    
    @BeforeEach
    public void init() {
        createUserDTO.setEmail("jborgex@gmail.com");
        createUserDTO.setFirstName("Jose");
        createUserDTO.setLastName("Borge");
        createUserDTO.setLastName2("Torres");
        createUserDTO.setNickname("josborg");
        createUserDTO.setNif("123456789N");
        createUserDTO.setPassword("1234".hashCode() + "");
        createUserDTO.setPhone("+34683317517");
        createUserDTO.setStatusUser("ACTIVO");
        
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
        
    }
    
    @Test
    void userEntitytoUserDTOTest() {
        
        UserDTO userDTO = userMaper.userEntitytoUserDTO(user);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(user.getCreatedAt()),
                    userDTO.getCreatedAt());
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
    void userEntityNulltoUserDTOTest() {
        UserDTO userDTO = userMaper.userEntitytoUserDTO(null);
        Assertions.assertNull(userDTO);
    }
    
    //    @Test
    //    void userEntityNullAtributestoUserDTOTest() {
    //        user.setCanceledAt(null);
    //        user.setCreatedAt(null);
    //        user.setUpdatedAt(null);
    //        user.setId(null);
    //        
    //        UserDTO userDTO = userMaper.userEntitytoUserDTO(null);
    //        Assertions.assertAll(() -> {
    //            Assertions.assertNull(userDTO.getCanceledAt());
    //        }, () -> {
    //            Assertions.assertNull(userDTO.getCreatedAt());
    //        }, () -> {
    //            Assertions.assertNull(userDTO.getUpdatedAt());
    //        }, () -> {
    //            Assertions.assertNull(userDTO.getId());
    //        });
    //    }
    
    @Test
    void createUserDTOtoUserEntityTest() {
        User user = userMaper.userDTOtoUserEntity(createUserDTO);
        Assertions.assertAll(() -> {
            Assertions.assertEquals(createUserDTO.getPassword(), user.getPassword());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getEmail(), user.getEmail());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getFirstName(), user.getFirstName());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getLastName(), user.getLastName());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getLastName2(), user.getLastName2());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getNickname(), user.getNickname());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getNif(), user.getNif());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getPhone(), user.getPhone());
        }, () -> {
            Assertions.assertEquals(createUserDTO.getStatusUser(), user.getStatusUser());
        });
    }
    
    @Test
    void createUserNullDTOtoUserEntityTest() {
        User user = userMaper.userDTOtoUserEntity(null);
        Assertions.assertNull(user);
    }
    
}
