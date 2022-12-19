package com.axpe.example.persistence.entities;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "USERS")
@SequenceGenerator(name = "sq_users", sequenceName = "sq_users", initialValue = 1, allocationSize = 1)
public class User {
    
    @With
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_users")
    private Long id;
    
    @Column(name = "name", nullable = false, length = 50)
    private String firstName;
    
    @Column(name = "surname1", nullable = true, length = 50)
    private String lastName;
    
    @Column(name = "surname2", nullable = true, length = 50)
    private String lastName2;
    
    @Column(name = "email", nullable = true, length = 255)
    private String email;
    
    @Column(name = "phone_number", nullable = true, length = 15)
    private String phone;
    
    @Column(name = "nif", nullable = false, length = 11)
    private String nif;
    
    @Column(name = "nickname", nullable = false, length = 255)
    private String nickname;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "status", nullable = true, length = 255)
    private String statusUser;
    
    @CreatedDate
    @Column(name = "entry_date", nullable = true)
    private LocalDateTime createdAt;
    
    @Column(name = "cancel_date", nullable = true)
    private LocalDateTime canceledAt;
    
    @LastModifiedDate
    @Column(name = "modified_date", nullable = true)
    private LocalDateTime updatedAt;
    
}
