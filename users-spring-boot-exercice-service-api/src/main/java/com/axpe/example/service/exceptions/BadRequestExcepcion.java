/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.service.exceptions;

/**
 * Excepción que define un error al producirse un conflicto
 * 
 * @author autor@axpe.com
 */
public class BadRequestExcepcion extends DevelopmentException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 8869264216498312812L;
    
    public BadRequestExcepcion(ErrorCode pErrorCode) {
        super(pErrorCode);
    }
    
    public BadRequestExcepcion(ErrorCode pErrorCode, Exception newOriginalException,
            Object... pArguments) {
        super(pErrorCode, newOriginalException, pArguments);
    }
    
    public BadRequestExcepcion(ErrorCode pErrorCode, Exception newOriginalException) {
        super(pErrorCode, newOriginalException);
    }
    
    public BadRequestExcepcion(ErrorCode pErrorCode, Object... pArguments) {
        super(pErrorCode, pArguments);
    }
    
}
