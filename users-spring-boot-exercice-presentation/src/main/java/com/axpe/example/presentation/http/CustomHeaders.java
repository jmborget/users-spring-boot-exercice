/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.presentation.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Clase que define las cabeceras específicas del Servicio
 * 
 * @author autor@axpe.com
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomHeaders {
	/** Identificador único de la petición */
	public static final String X_REQUEST_ID = "X-Request-ID";
	public static final String LOCATION = "Location";
	
	public static final String CONTENT_TYPE = "Content-Type";
}
