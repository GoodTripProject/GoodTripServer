package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.models.User;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class AuthenticationTests {
    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Test
    public void login_UserDoesNotExist_FailedLogin() {
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.findUserByUsernameAndHashedPassword("ab", "ab"),
                Optional.empty());
    }

    @Test
    public void authentication_UserDoesNotExist_UserLoggedIn() {
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.findUserByUsernameAndHashedPassword("a", "c"));
        authenticationRepository.addUserIfNotExists("a", "b", "c", "e", "f", "g");
        Optional<User> user = authenticationRepository.findUserByUsernameAndHashedPassword("a", "c");
        assertTrue(user.isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());

        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.findUserByUsernameAndHashedPassword("a", "c"));
    }

    @Test
    public void signUp_UserDoesNotExist_SignUpSeveralTimes() {
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword(
                "a", "c");
        assertTrue(authenticationRepository.addUserIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.addUserIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.addUserIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.addUserIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        authenticationRepository.deleteUserIfExistsByUsernameAndHashedPassword(
                "a", "c");
        assertEquals(Optional.empty(),
                authenticationRepository.findUserByUsernameAndHashedPassword("a","c"));
    }

}
