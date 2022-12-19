package com.axpe.example.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import com.axpe.example.service.dto.CreateValidateEmailDTO;

@ExtendWith(MockitoExtension.class)
@Service
class ApiEmailsImplTest {
    
    @InjectMocks
    private ApiEmailsImpl apiEmailsService;
    
    @BeforeEach
    public void init() {
        
    }
    
    @Test
    void isValidTest() {
        
        CreateValidateEmailDTO createValidation = new CreateValidateEmailDTO("jose@gmail.com");
        Assertions.assertTrue(apiEmailsService.validate(createValidation).isValid());
    }
    
    //    @Test
    //    void validateCreateValidationStatusNullTest() {
    //        
    //        CreateValidateEmailDTO createValidation = new CreateValidateEmailDTO("jose@gmail.com");
    //        Assertions.assertThrows(ContentNotFoundException.class, () -> {
    //            apiEmailsService.validate(createValidation);
    //        });
    //    }
    
}
