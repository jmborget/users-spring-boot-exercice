package com.axpe.example.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.axpe.example.service.ApiEmails;
import com.axpe.example.service.dto.CreateValidateEmailDTO;
import com.axpe.example.service.dto.ValidationDTO;
import com.axpe.example.service.exceptions.ContentNotFoundException;
import com.axpe.example.service.exceptions.ErrorCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApiEmailsImpl implements ApiEmails {
    
    private static final String INVALID = "invalid";
    private static final String UNKNOWN = "unknown";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String X_TOMBA_KEY = "X-Tomba-Key";
    private static final String X_TOMBA_SECRET = "X-Tomba-Secret";
    
    private static final String CONTENT_TYPE_VALUE = "application/json";
    @Value("${tomba.key:ta_db2bed5694d804118d1dae15d093199772cfd}")
    private String keyValue = "ta_db2bed5694d804118d1dae15d093199772cfd";
    @Value("${tomba.value:ts_a82debf1-d63d-4a73-b973-d2c491b5e328}")
    private String secretValue = "ts_a82debf1-d63d-4a73-b973-d2c491b5e328";
    
    @Override
    public ValidationDTO validate(CreateValidateEmailDTO createValidation) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, CONTENT_TYPE_VALUE);
        headers.add(X_TOMBA_KEY, keyValue);
        headers.add(X_TOMBA_SECRET, secretValue);
        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tomba.io/v1/email-verifier/" + createValidation.getEmail(),
                HttpMethod.GET, request, String.class, 1);
        
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            ObjectMapper mapper = new ObjectMapper();
            
            try {
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode data = root.path("data");
                JsonNode email = data.path("email");
                return new ValidationDTO(!(INVALID.equals(email.get("status").asText()))
                        && !(UNKNOWN.equals(email.get("status").asText())));
            } catch (Exception ex) {
                throw new ContentNotFoundException(ErrorCode.GENERIC_INFRASTRUCTURE, ex);
            }
        } else {
            throw new ContentNotFoundException(ErrorCode.INVALID_VALUE);
        }
        //        return new ValidationDTO(true);
    }
}
