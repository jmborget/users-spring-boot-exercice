package com.axpe.example.service.dto;

import org.springframework.data.domain.Pageable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ParamsDTO {
    
    private String firstName;
    private String lastName;
    private String lastName2;
    private String email;
    private String nif;
    private String phone;
    private String nickname;
    private String statusUser;
    private String sortBy;
    private Pageable pageable;
}
