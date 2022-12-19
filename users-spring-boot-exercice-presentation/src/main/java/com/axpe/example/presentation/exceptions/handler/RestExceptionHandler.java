/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.presentation.exceptions.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.axpe.example.service.exceptions.BadRequestExcepcion;
import com.axpe.example.service.exceptions.ConflictExcepcion;
import com.axpe.example.service.exceptions.ContentNotFoundException;
import com.axpe.example.service.exceptions.ErrorCode;
import com.axpe.example.service.exceptions.ErrorMessage;
import com.axpe.example.service.exceptions.ErrorView;
import com.axpe.example.service.exceptions.ForbiddenException;
import com.axpe.example.service.exceptions.TypeErrorEnum;

/**
 * Manejador de excepciones para el API
 * 
 * @author autor@axpe.com
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(BadRequestExcepcion.class)
    public ResponseEntity<Object> handleBadRequestExcepcion(BadRequestExcepcion ex) {
        ErrorMessage errorMessage = ErrorMessage.builder().code(ex.getErrorCode().getCode())
                .message(ex.getMessage()).type(TypeErrorEnum.ERROR).description("Bad Request")
                .build();
        ErrorView error = ErrorView.builder().message(errorMessage).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<Object> handleContentNotFoundException(ContentNotFoundException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder().code(ex.getErrorCode().getCode())
                .message(ex.getMessage()).type(TypeErrorEnum.ERROR).description("Not Found")
                .build();
        ErrorView error = ErrorView.builder().message(errorMessage).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ConflictExcepcion.class)
    public ResponseEntity<Object> handleConflictExcepcion(ConflictExcepcion ex) {
        ErrorMessage errorMessage = ErrorMessage.builder().code(ex.getErrorCode().getCode())
                .message(ex.getMessage()).type(TypeErrorEnum.ERROR).description("Conflict").build();
        ErrorView error = ErrorView.builder().message(errorMessage).build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        ErrorMessage errorMessage = ErrorMessage.builder().code(ex.getErrorCode().getCode())
                .message(ex.getMessage()).type(TypeErrorEnum.ERROR).description("Forbidden")
                .build();
        ErrorView error = ErrorView.builder().message(errorMessage).build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
    
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ContentNotFoundException newEx = new ContentNotFoundException(ErrorCode.UNKNOWN);
        ErrorMessage errorMessage = ErrorMessage.builder().code(newEx.getErrorCode().getCode())
                .message(ex.getMessage()).type(TypeErrorEnum.ERROR).description("Bad Request")
                .build();
        ErrorView error = ErrorView.builder().message(errorMessage).build();
        return ResponseEntity.status(status).headers(headers).body(error);
        //return super.handleExceptionInternal(ex, body, headers, status, request);
        
    }
    
}
