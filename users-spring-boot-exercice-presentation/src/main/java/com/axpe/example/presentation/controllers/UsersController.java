package com.axpe.example.presentation.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.axpe.example.service.dto.UserDTO;
import com.github.fge.jsonpatch.JsonPatch;

/**
 * Interfaz del controlador de usuarios, define la especificación de las peticiones que podrá
 * procesar el servicio basada en la API especificada.
 * 
 * @author jmborge-local
 *
 */
public interface UsersController {
    
    public static final String BASE_URL = "/users";
    public static final String URL_ID = "/{id}";
    public static final String PATH_VARIABLE_NAME = "id";
    
    public static final int PAGE = 0;
    public static final int SIZE = 10;
    
    /**
     * Petición POST:/users Requiere un UserDTO válido como entrada, si es valido comprobara que
     * cumple la condiciones para ser guardado, estas condiciones son que ni el nikname, email, nif
     * ni teléfono estén ya registrados en el sistema. Si no cumple las condiciones registrado se
     * producirá una respuesta de 409. Si se guarda el usuario correctamente la respuesta será 201 y
     * en la respuesta existirá una cabecera Location con la ruta del recurso creado.
     * 
     * @param newUser
     *        - DTO de usuario
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Respuesta vacía (sin body)
     */
    public ResponseEntity<Void> newUser(@Valid final UserDTO newUser, final String xRequestId);
    
    /**
     * Petición GET:/users?{ firstName, lastName, lastName2, email, nif, pone, nickname, statusUser,
     * page, size, sort} Solicita un listado paginado de los usuarios del sistema según los filtros
     * especificados. La respuesta será 200 si esta es completada
     * 
     * @param firstName
     *        - Nombre del usurio
     * @param lastName
     *        - Primer apellido
     * @param lastName2
     *        - Segundo apellido
     * @param email
     *        - Correo electrónico
     * @param nif
     *        - Documento de identificacion
     * @param phone
     *        - Telefono
     * @param nickname
     *        - Alias del usuario
     * @param statusUser
     *        - Estado
     * @param pageable
     *        - parametro de paginación {page, size, sort}
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Página que contiene los usuarios
     */
    public ResponseEntity<Page<UserDTO>> listAllUsers(final String firstName, final String lastName,
            final String lastName2, final String email, final String nif, final String phone,
            final String nickname, final String statusUser, final Pageable pageable,
            final String xRequestId);
    
    /**
     * Petición GET:/users/{id} Devuelve un usuario por id. Si lo encuentra la respuesta constará de
     * un body con el usuario y una respuesta 200. Si no encuentra al usuario con el id especificado
     * retornara un error 404.
     * 
     * @param id
     *        - Número de identificación de usuario
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Usuario solicitado
     */
    public ResponseEntity<UserDTO> getUserById(final Long id, final String xRequestId);
    
    /**
     * Petición PUT:/users/{id} Requiere un UserDTO válido como entrada, si es valido comprobara que
     * cumple la condiciones para ser guardado, estas condiciones son que ni el nikname, email, nif
     * ni teléfono estén ya registrados en el sistema. Si no cumple las condiciones registrado se
     * producirá una respuesta de 409. Si se guarda el usuario correctamente la respuesta será 204
     * 
     * @param id
     *        - Número de identificación de usuario
     * @param editUser
     *        - DTO de usuario
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Respuesta vacía (sin body)
     */
    public ResponseEntity<Void> putUserById(final Long id, @Valid final UserDTO editUser,
            final String xRequestId);
    
    /**
     * Petición DELETE:/users/{id} Realiza el borrado de un usuario del sistema, este borrado es un
     * borrado lógico es decir lo que realiza es un cambio de estado del usuario a 'DELETE'. Si el
     * borrado se realiza correctamente se devuelve una respuesta 204. En caso contrario si el
     * usuario con el id especificado no existe se producirá una respuesta 409.
     * 
     * @param id
     *        - Número de identificación de usuario
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Respuesta vacía (sin body)
     */
    public ResponseEntity<Void> deleteUserById(final Long id, final String xRequestId);
    
    /**
     * Petición PATCH:/users/{id} Realiza una edición parcial de un usuario siguiendo las
     * condiciones de la petición PUT:/users/{id}
     * 
     * @param id
     *        - Número de identificación de usuario
     * @param editUser
     *        - DTO parcial de usuario
     * @param xRequestId
     *        - Cabecera opcional para el debugeo, para la trazabilidad
     * @return Usuario modificado
     */
    public ResponseEntity<UserDTO> patchUserById(final Long id, final Map<Object, Object> fields,
            String xRequestId);
    
    public ResponseEntity<UserDTO> updateUserById(final Long id, final JsonPatch patchUser,
            final String xRequestId);
}
