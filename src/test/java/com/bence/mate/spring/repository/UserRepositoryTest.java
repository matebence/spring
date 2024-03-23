package com.bence.mate.spring.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.entity.UserEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenSaveIsTriggered_thenNewRecordInDbShouldBeCreated() {
        // given
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("ecneb");
        userEntity.setFirstName("Bence");
        userEntity.setLastName("Mate");
        userEntity.setUserRole("ADMIN");

        // when
        userRepository.save(userEntity);

        // then
        Optional<UserEntity> optionalExpected = userRepository.findByUsername("ecneb");
        UserEntity expected = optionalExpected.get();

        assertNotNull(expected);

        assertEquals(expected.getUsername(), userEntity.getUsername());
        assertEquals(expected.getFirstName(), userEntity.getFirstName());
        assertEquals(expected.getLastName(), userEntity.getLastName());
        assertEquals(expected.getUserRole(), userEntity.getUserRole());
    }
}
