package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.models.User;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepositoryImplementation;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTests {
    AuthenticationRepository authenticationRepository = new AuthenticationRepositoryImplementation();

    @Test
    public void login_UserDoesNotExist_FailedLogin() {
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("ab", "ab"),
                Optional.empty());
    }

    @Test
    public void authentication_UserDoesNotExist_UserLoggedIn() {
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
        authenticationRepository.signUpIfNotExists("a", "b", "c", "e", "f", "g");
        Optional<User> user = authenticationRepository.login("a", "c");
        assertTrue(user.isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());

        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
    }

    @Test
    public void signUp_UserDoesNotExist_SignUpSeveralTimes() {
        authenticationRepository.deleteUserIfExists(
                "a", "c");
        assertTrue(authenticationRepository.signUpIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpIfNotExists(
                "a", "b", "c", "e", "f", "g"));
        authenticationRepository.deleteUserIfExists(
                "a", "c");
        assertEquals(Optional.empty(),
                authenticationRepository.login("a","c"));
    }

}
