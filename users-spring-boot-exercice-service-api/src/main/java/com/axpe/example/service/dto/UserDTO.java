package com.axpe.example.service.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

/**
 * Clase que representa los objetos de transferencia de los datos de usuarios.
 * 
 * En esta clase se definen los atributos de los que dispone un usuario, las condiciones de acceso a
 * estos atributos, los formatos de cada atributo y cuales son obligatorios.
 * 
 * @author jmborge-local
 *
 */
@RequiredArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    
    /**
     * Número de identificación de usuario. No puede ser editado.
     */
    @With
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;
    
    /**
     * Nombre de usuario, no puede estar vacío, el tamaño máximo serán 50 caracteres.
     */
    @NotEmpty
    @Size(max = 50)
    private String firstName;
    
    /**
     * Primer apellido de usuario, puede ser nulo, el tamaño máximo serán 50 caracteres.
     */
    @Size(max = 50)
    private String lastName;
    
    /**
     * Segundo apellido de usuario, puede ser nulo, el tamaño máximo serán 50 caracteres.
     */
    @Size(max = 50)
    private String lastName2;
    
    /**
     * Email de usuario, no puede estar vacío, debe tener el formato de Email definido en Estándar
     * RFC 5322, el tamaño máximo serán 255.
     */
    @Pattern(regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$")
    @Email
    @NotEmpty
    @Size(max = 255)
    private String email;
    
    /**
     * Teléfono de usuario, no puede estar vacío, el tamaño máximo serán 15 caracteres.
     */
    @NotEmpty
    @Size(max = 15)
    private String phone;
    
    /**
     * Nif de usuario, no puede estar vacío, el tamaño máximo serán 11 caracteres.
     */
    @NotEmpty
    @Size(max = 11)
    private String nif;
    
    /**
     * Nickname de usuario, no puede estar vacío, el tamaño máximo serán 255 caracteres.
     */
    @NotEmpty
    @Size(max = 255)
    private String nickname;
    
    /**
     * Contraseña de usuario, no puede estar vacío, el tamaño máximo serán 255 caracteres. No puede
     * ser leído.
     */
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(max = 255)
    private String password;
    
    /**
     * Estado de usuario, puede ser nulo, el tamaño máximo serán 255 caracteres.
     */
    @Size(max = 255)
    private String statusUser;
    
    /**
     * Momento de creación de usuario, no puede ser editado, sigue el formato ISO.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createdAt;
    
    /**
     * Momento de creación de cancelación, no puede ser editado, sigue el formato ISO.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String canceledAt;
    
    /**
     * Momento de creación de edición, no puede ser editado, sigue el formato ISO.
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updatedAt;
}
