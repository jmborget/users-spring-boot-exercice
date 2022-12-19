/*******************************************************************************
 * 
 * Autor: autor@axpe.com
 * 
 * © Axpe Consulting S.L. 2022. Todos los derechos reservados.
 * 
 ******************************************************************************/

package com.axpe.example.persistence.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.axpe.example.persistence.entities.User;

/**
 * Interfaz que define el acceso a la tabla de USERS
 * 
 * @author autor@axpe.com
 *
 */
//JpaRepository
@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, Long> {
    
    /**
     * Comprueba si un usuario es guardable, se considera guardable si no existe usuario con email,
     * nif, teléfono o nickname iguales a los del usuario que se quiere guardar.
     * 
     * @param usr
     *        - Usuario para comprobar
     * @return
     */
    // @formatter:off
    @Query("select case when count(u)> 0 then false else true end " 
            + "from User u where "
            + "(lower(u.email) = lower(:#{#usr.email}) or "
            + "u.phone = :#{#usr.phone} or " + "lower(u.nif) = lower(:#{#usr.nif}) or "
            + "u.nickname = :#{#usr.nickname})")
    // @formatter:on
    boolean isSaveable(@Param("usr") final User usr);
    
    /**
     * Comprueba si un usuario es editable, se considera editable si no existe usuario con email,
     * nif, teléfono o nickname iguales a los del usuario salvo que sean los del mismo usuario
     * 
     * @param id
     *        - Número de identificación del usuario
     * @param usr
     *        - Datos Usuario para comprobar
     * @return
     */
    // @formatter:off
    @Query("select case when count(u)> 0 then false else true end " 
            + "from User u where "
            + "(u.id <> :id) and " + "(lower(u.email) = lower(:#{#usr.email}) or "
            + "u.phone = :#{#usr.phone} or " + "lower(u.nif) = lower(:#{#usr.nif}) or "
            + "u.nickname = :#{#usr.nickname})")
    // @formatter:on
    boolean isEditable(@Param("id") final Long id, @Param("usr") final User usr);
    
    /**
     * Retorna una página de usuario en función de los parameros especificados.
     * 
     * @param firstName
     * @param lastName
     * @param lastName2
     * @param email
     * @param phone
     * @param nif
     * @param nickname
     * @param statusUser
     * @param pageable
     * @return Página de usuarios.
     */
    // count parametro
    // @formatter:off
    @Query("select u "
            + "from User u where "
            + "(:firstName is null or u.firstName = :firstName) and "
            + "(:lastName is null or u.lastName = :lastName) and "
            + "(:lastName2 is null or u.lastName2 = :lastName2) and "
            + "(:email is null or u.email = :email) and "
            + "(:phone is null or u.phone = :phone) and " 
            + "(:nif is null or u.nif = :nif) and "
            + "(:nickname is null or u.nickname = :nickname) and "
            + "(:statusUser is null or u.statusUser = :statusUser)")
    // @formatter:on
    public Page<User> findByParams(@Param("firstName") final String firstName,
            @Param("lastName") final String lastName, @Param("lastName2") final String lastName2,
            @Param("email") final String email, @Param("phone") final String phone,
            @Param("nif") final String nif, @Param("nickname") final String nickname,
            @Param("statusUser") final String statusUser, Pageable pageable);
    
    /**
     * Edita el estado del usuario con id especificado a ‘DELETE’ y camba la fecha de edición a la
     * fecha pasada por parámetro.
     * 
     * @param id
     *        - Número de identificación del usuario
     * @param localDateTime
     *        - Fecha en la que se modifica el usuario
     * @return Números de usuarios modificados.
     */
    @Modifying(clearAutomatically = true) //Importante para los test
    @Transactional
    @Query("update User u SET u.statusUser = 'DELETE', u.canceledAt = :localDateTime  where u.id = :id")
    int updateStatusDelete(@Param("id") final Long id,
            @Param("localDateTime") final LocalDateTime localDateTime);
    //@Query("update User u SET u.statusUser = 'DELETE', u.canceledAt = ?#{T(java.time.LocalDateTime).now()}  where u.id = :id")
}