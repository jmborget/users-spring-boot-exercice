package com.axpe.example.presentation.controllers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.axpe.example.presentation.controllers.ValidateEmailsController;
import com.axpe.example.presentation.http.CustomHeaders;
import com.axpe.example.service.ApiEmails;
import com.axpe.example.service.dto.CreateValidateEmailDTO;
import com.axpe.example.service.dto.ValidationDTO;

@RestController
public class ValidateEmailsControllerImpl implements ValidateEmailsController {
    
    @Autowired
    private ApiEmails apiEmail;
    
    @Override
    public ResponseEntity<ValidationDTO> validateEmail(CreateValidateEmailDTO newEmail,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId) {
        ValidationDTO validationDTO = this.apiEmail.validate(newEmail);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(CustomHeaders.X_REQUEST_ID, xRequestId);
        return new ResponseEntity<>(validationDTO, headers, HttpStatus.OK);
        
    }
    
}
