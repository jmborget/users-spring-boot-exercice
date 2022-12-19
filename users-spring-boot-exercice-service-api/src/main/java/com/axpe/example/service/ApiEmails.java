package com.axpe.example.service;

import javax.validation.Valid;

import com.axpe.example.service.dto.CreateValidateEmailDTO;
import com.axpe.example.service.dto.ValidationDTO;

public interface ApiEmails {
    
    public ValidationDTO validate(@Valid CreateValidateEmailDTO validation);
    
}
