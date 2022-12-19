package com.axpe.example.persistence.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import com.axpe.example.persistence.entities.User;

import lombok.RequiredArgsConstructor;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@Sql(scripts = "classpath:db/data-h2.sql")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UsersDAOTest {
    
    private final TestEntityManager entityManager;
    
    private final UsersRepository usersRepository;
    
    User user;
    
    @BeforeEach
    public void init() {
        user = new User();
        user.setEmail("jborgex@gmail.com");
        user.setFirstName("Jose");
        user.setLastName("Borge");
        user.setLastName2("Torres");
        user.setNickname("josborg");
        user.setPassword("1234".hashCode() + "");
        user.setNif("123456789N");
        user.setPhone("+34683317517");
        user.setStatusUser("ACTIVO");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setCanceledAt(LocalDateTime.now());
    }
    
    @Test
    @Transactional
    void whenFindById_thenReturnUser() {
        // given
        entityManager.persist(user);
        entityManager.flush();
        
        // when
        Optional<User> userFound = usersRepository.findById(user.getId());
        
        // then
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getEmail(), is(user.getEmail()));
    }
    
    @Test
    @Transactional
    void whenSave_thenReturnUserSaved() {
        // given
        
        // when
        User userEntity = usersRepository.save(user);
        Optional<User> userFound = usersRepository.findById(user.getId());
        
        // then
        assertThat(userEntity.getId(), is(user.getId()));
        assertThat(userEntity.getEmail(), is(user.getEmail()));
        assertTrue(userFound.isPresent());
        assertThat(userFound.get().getEmail(), is(user.getEmail()));
    }
    
    @Test
    @Transactional
    void whenSaveTwiceTheSameUser_thenReturnNumberOfSavedUsersEqualsTo1() {
        // given
        User userEntity = usersRepository.save(user);
        
        // when
        usersRepository.save(userEntity);
        
        // then
        assertThat(usersRepository.count(), is(5L));
        assertThat(userEntity.getId(), is(user.getId()));
        assertThat(userEntity.getEmail(), is(user.getEmail()));
        
    }
    
    @Test
    @Transactional
    void whenSaveTwiceDistinctUsers_thenReturnNumberOfSavedUsersEqualsTo2() {
        // given
        User userEntity = usersRepository.save(user).withId(null);
        
        // when
        usersRepository.save(userEntity);
        
        //then
        assertThat(usersRepository.count(), is(6L));
        
    }
    
    @Test
    @Transactional
    void whenFindByParams_thenReturnPageUser() {
        // given
        
        // when
        Page<User> pageUsers = usersRepository.findByParams(null, null, null, null, null, null,
                null, null, Pageable.ofSize(10));
        
        // then
        assertTrue(pageUsers.isFirst());
        assertFalse(pageUsers.isEmpty());
        assertThat(pageUsers.getContent().size(), is(4));
    }
    
    @Test
    @Transactional
    void whenUpdateStatusDelete_thenReturnNumberOfUsersEdited() {
        // given
        usersRepository.save(user);
        
        // when
        int n = usersRepository.updateStatusDelete(user.getId(), LocalDateTime.now());
        Optional<User> userFound = usersRepository.findById(user.getId());
        
        // then
        assertThat(usersRepository.count(), is(5L));
        assertThat(n, is(1));
        assertThat(userFound.get().getId(), is(user.getId()));
        assertThat(userFound.get().getStatusUser(), is("DELETE"));
    }
    
    @Test
    @Transactional
    void whenIsSaved_thenReturnIsNotSalvable() {
        // given
        usersRepository.save(user);
        
        // when
        Optional<User> userFound = usersRepository.findById(user.getId());
        boolean n = usersRepository.isSaveable(userFound.get());
        
        // then
        assertFalse(n);
    }
    
    @Test
    @Transactional
    void whenIsEditable_thenReturnIsEditable() {
        // given
        usersRepository.save(user);
        
        // when
        Optional<User> userFound = usersRepository.findById(user.getId());
        boolean n = usersRepository.isEditable(user.getId(), userFound.get());
        
        // then
        assertTrue(n);
    }
    
    @Test
    @Transactional
    void whenIsNotEditable_thenIsNotEditable() {
        // given
        usersRepository.save(user);
        
        // when
        Optional<User> userFound = usersRepository.findById(user.getId());
        boolean n = usersRepository.isEditable(user.getId() + 1, userFound.get());
        
        // then
        assertFalse(n);
    }
    
    @AfterEach
    public void terminate() {
        usersRepository.deleteAll();
        entityManager.clear();
        entityManager.flush();
    }
}