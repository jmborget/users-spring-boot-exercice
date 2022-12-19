/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.service.exceptions;

/**
 * Interfaz de la que heredaran las excepciones de la aplicación
 * 
 * @author autor@axpe.com
 */
public interface GenericException {

	/**
	 * Retorna el código de error.
	 * @return Código que identifica el error producido
	 */
	ErrorCode getErrorCode();
}
