/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.service.exceptions;

/**
 * Listado de las validaciones identificadas así como los códigos y mensajes de
 * error que se deben retornar a los clientes
 * 
 * @author autor@axpe.com
 * 
 */
public enum ErrorCode {

	NOT_AUTHENTICATED("Usuario no autenticado"),
	INVALID_AUTHENTICATION("No se han encontrado las credenciales en el contexto"),
	ACCESS_DENIED("Acceso no permitido al objeto u operacion {0}"),
	MANDATORY_PROPERTY("Propiedad {0} no puede estar vacía"),
	INVALID_VALUE("Valor inválido para la propiedad {0}"),
	OBJECT_NOT_FOUND("Objeto no encontrado"),
	OBJECT_MODIFIED("Objecto modificado durante la transacción"),
	
	GENERIC_INFRASTRUCTURE("Error genérico de infraestructura"),
	GENERIC_DEVELOPMENT("Error genérico de desarrollo"),

	UNEXPECTED_INFRASTRUCTURE("Inexperado error de infraestructura"),
	UNEXPECTED_DEVELOPMENT("Inexperado error de desarrollo"),

	UNKNOWN("Desconocido");

	/**
	 * Código de la excepción
	 */
	private final String code;

	/**
	 * Mensaje adicional de la excepción
	 */
	private final String message;

	/**
	 * Constructor
	 * <p>
	 * Permite indicar un mensaje adicional que se guarda junto al mensaje
	 * básico asociado al código
	 * 
	 * @param message
	 *            Mensaje adicional
	 */
	private ErrorCode(final String message) {
		this.code = this.name();
		this.message = message;
	}

	/**
	 * Retorna el código de la excepción
	 * 
	 * @return Código de la excepción
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Retorna el mensaje adicional de la excepción
	 * 
	 * @return Mensaje de la excepción
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Conversión de la clase a String
	 * 
	 * @return El código de la excepción en texto
	 */
	@Override
	public String toString() {
		return code;
	}
}
