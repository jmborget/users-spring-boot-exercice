package com.axpe.example.presentation.controllers;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.axpe.example.service.dto.CreateValidateEmailDTO;
import com.axpe.example.service.dto.ValidationDTO;

public interface ValidateEmailsController {
    
    static final String BASE_URL = "/validate";
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, value = { BASE_URL })
    public ResponseEntity<ValidationDTO> validateEmail(
            @Valid @RequestBody CreateValidateEmailDTO newEmail,
            @RequestHeader(value = "X-Request-ID", required = false) String xRequestId);
}
