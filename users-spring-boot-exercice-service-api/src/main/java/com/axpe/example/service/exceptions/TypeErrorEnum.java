/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.service.exceptions;

/**
 * Error message severity.
 * 
 * @author  autor@axpe.com
 *
 */
public enum TypeErrorEnum {
	/** error that prevents the operation of the entire application. */
	CRITICAL,
	/** error that prevents the operation of a specific functionality */
	FATAL,
	/** error that does not affect more features */
	ERROR,
	/** application works but a warning has been generated */
	WARNING,
	/** operative info message */
	INFO

}
