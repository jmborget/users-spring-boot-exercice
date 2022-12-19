package com.axpe.example.service.utils;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
// import org.mapstruct.Mapping;
// import org.mapstruct.factory.Mappers;

import com.axpe.example.persistence.entities.User;
import com.axpe.example.service.dto.UserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMaper {
    
    UserDTO userEntitytoUserDTO(User user);
    
    User userDTOtoUserEntity(UserDTO userDTO);
}
