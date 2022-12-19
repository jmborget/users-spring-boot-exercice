package com.axpe.example.service;

import java.util.Map;

import org.springframework.data.domain.Page;

import com.axpe.example.service.dto.ParamsDTO;
import com.axpe.example.service.dto.UserDTO;
import com.github.fge.jsonpatch.JsonPatch;

public interface ApiUsers {
    
    /**
     * Comprueba si la contraseña del usuario con id especificado es igual a la contraseña pasado
     * por parámetro.
     * 
     * @param id
     *        - Número de identificación del usuario.
     * @param pass
     *        - Cadena de caracteres que forma la contraseña.
     * @return - Devuelve {true} si coincide {false} si no.
     */
    public boolean isPasswordUser(final Long id, final String pass);
    
    /**
     * Guarda un Usuario en el sistema, el Usuario tiene que ser valido, caso de no serlo no
     * guardará el Usuario, si lo gurda retornara el Usuario guardado.
     * 
     * @param user
     *        - DTO de Usuario.
     * @return DTO de Usuario guardado.
     */
    public UserDTO save(final UserDTO user);
    
    /**
     * Edita los atributos del usuario con id especificado, sustituyendo los valores del mismo por
     * los de el usuario pasado por parámetro.
     * 
     * @param id
     *        - Número de identificación del usuario.
     * @param user
     *        - DTO de Usuario.
     */
    public void edit(final Long id, UserDTO user);
    
    /**
     * Elimina el usuario con id especificado cambiando el estado del usuario a ‘DELETE’.
     * 
     * @param id
     *        - Número de identificación del usuario.
     */
    public void delete(final Long id);
    
    /**
     * Retorna el usuario con id especificado.
     * 
     * @param id
     *        - Número de identificación del usuario.
     * @return DTO de Usuario.
     */
    public UserDTO getUserById(final Long id);
    
    /**
     * Edita parcialmente el usuario con id especificado en función de los atributos pasados por
     * parámetro.
     * 
     * @param id
     *        - Número de identificación del usuario.
     * @param fields
     * @return DTO de Usuario.
     */
    public UserDTO patchUserById(final Long id, final Map<Object, Object> fields);
    
    /**
     * Retorna una página de usuarios en función de los parameros especificados.
     * 
     * @param params
     *        - DTO de parametros.
     * @return Página de Usuarios.
     */
    public Page<UserDTO> findByParams(final ParamsDTO params);
    
    public UserDTO updateUserById(final Long id, final JsonPatch patch);
}