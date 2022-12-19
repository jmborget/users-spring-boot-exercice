package com.axpe.example.service.utils;

import com.axpe.example.persistence.entities.User;
import com.axpe.example.service.dto.UserDTO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-12-16T13:53:11+0100",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4.1 (Eclipse Adoptium)"
)
@Component
public class UserMaperImpl implements UserMaper {

    @Override
    public UserDTO userEntitytoUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        if ( user.getId() != null ) {
            userDTO.id( user.getId() );
        }
        userDTO.firstName( user.getFirstName() );
        userDTO.lastName( user.getLastName() );
        userDTO.lastName2( user.getLastName2() );
        userDTO.email( user.getEmail() );
        userDTO.phone( user.getPhone() );
        userDTO.nif( user.getNif() );
        userDTO.nickname( user.getNickname() );
        userDTO.password( user.getPassword() );
        userDTO.statusUser( user.getStatusUser() );
        if ( user.getCreatedAt() != null ) {
            userDTO.createdAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getCreatedAt() ) );
        }
        if ( user.getCanceledAt() != null ) {
            userDTO.canceledAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getCanceledAt() ) );
        }
        if ( user.getUpdatedAt() != null ) {
            userDTO.updatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getUpdatedAt() ) );
        }

        return userDTO.build();
    }

    @Override
    public User userDTOtoUserEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDTO.getId() );
        user.firstName( userDTO.getFirstName() );
        user.lastName( userDTO.getLastName() );
        user.lastName2( userDTO.getLastName2() );
        user.email( userDTO.getEmail() );
        user.phone( userDTO.getPhone() );
        user.nif( userDTO.getNif() );
        user.nickname( userDTO.getNickname() );
        user.password( userDTO.getPassword() );
        user.statusUser( userDTO.getStatusUser() );
        if ( userDTO.getCreatedAt() != null ) {
            user.createdAt( LocalDateTime.parse( userDTO.getCreatedAt() ) );
        }
        if ( userDTO.getCanceledAt() != null ) {
            user.canceledAt( LocalDateTime.parse( userDTO.getCanceledAt() ) );
        }
        if ( userDTO.getUpdatedAt() != null ) {
            user.updatedAt( LocalDateTime.parse( userDTO.getUpdatedAt() ) );
        }

        return user.build();
    }
}
