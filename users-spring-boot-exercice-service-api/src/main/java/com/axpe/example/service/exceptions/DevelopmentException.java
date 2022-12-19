/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.service.exceptions;

import java.text.MessageFormat;

/**
 * Excepción genérica de la aplicación
 * 
 * @author autor@axpe.com
 */
public class DevelopmentException extends RuntimeException implements GenericException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6356781781876201268L;
	
	private static final Object[] NO_ARGUMENTS = null;

	/** The errorCode ErrorCode */
	private ErrorCode errorCode;
	
	/** The args Object[] */
	private Object[] arguments;
	
	/**
	 * Consctructor con código de error
	 * @param pErrorCode código de error
	 */
	public DevelopmentException(ErrorCode pErrorCode) {
		super();
		this.setErrorCode(pErrorCode);
		this.arguments = NO_ARGUMENTS;
	}

	/**
	 * Constructor con código de error y argumentos
	 * @param pErrorCode código de error
	 * @param pArguments argumentos para almacenar
	 */
	public DevelopmentException(ErrorCode pErrorCode, Object... pArguments) {
		super();
		this.setErrorCode(pErrorCode);
		this.arguments = pArguments;
	}

	/**
	 * Constructor con parámetros
	 * @param pErrorCode código de error
	 * @param newOriginalException excepción original que encapsulamos
	 */
	public DevelopmentException(ErrorCode pErrorCode, Exception newOriginalException) {
		super(newOriginalException);
		this.setErrorCode(pErrorCode);
		this.arguments = NO_ARGUMENTS;
	}

	/**
	 * Constructor con parámetros
	 * @param pErrorCode código de error
	 * @param newOriginalException excepción original que encapsulamos
	 * @param pArguments  argumentos para almacenar
	 */
	public DevelopmentException(ErrorCode pErrorCode, Exception newOriginalException, Object... pArguments) {
		super(newOriginalException);
		this.setErrorCode(pErrorCode);
		this.arguments = pArguments;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		
		if ((this.arguments == null) || (this.arguments.length == 0)) {
			return this.errorCode.getMessage();
		}
		
		String formattedMessage = MessageFormat.format(this.errorCode.getMessage(), this.arguments);
		return formattedMessage;
	}

	/**
	 * Sets the errorCode
	 * @param errorCode the errorCode to set
	 */
	private void setErrorCode(ErrorCode errorCode) {
		
		this.errorCode = (errorCode == null) ? ErrorCode.UNKNOWN : errorCode;
	}

	/**
	 * Returns the errorCode
	 * @return the errorCode
	 */
	public ErrorCode getErrorCode() {
		
		return this.errorCode;
	}

}
