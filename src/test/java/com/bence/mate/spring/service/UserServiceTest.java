package com.bence.mate.spring.service;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.context.annotation.Import;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bence.mate.spring.entity.UserEntity;
import com.bence.mate.spring.pojo.UserPojo;

@DataJpaTest
@Import(UserService.class)
@TestPropertySource(locations = {"classpath:application-h2.properties"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void whenSaveIsTriggered_thenNewRecordInDbShouldBeCreated() {
        // given
        UserPojo userPojo = UserPojo.builder()
                .username("ecneb")
                .firstName("Bence")
                .lastName("Mate")
                .userRole("ADMIN").build();

        // when
        userService.save(userPojo);

        // then
        UserEntity expected = userService.findByUsername("ecneb");

        assertNotNull(expected);

        assertEquals(expected.getUsername(), userPojo.getUsername());
        assertEquals(expected.getFirstName(), userPojo.getFirstName());
        assertEquals(expected.getLastName(), userPojo.getLastName());
        assertEquals(expected.getUserRole(), userPojo.getUserRole());
    }
}
