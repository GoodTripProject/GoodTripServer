package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTests {
    AuthenticationRepository authenticationRepository = new AuthenticationRepository();


    @Test
    public void simpleLoginFailTest() {
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("ab", "ab"),
                Optional.empty());
    }

    @Test
    public void simpleSignUpTest() {
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
        authenticationRepository.signUp("a", "b", "c", "d", "e", "f", "g");
        assertTrue(authenticationRepository.login("a", "c").isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());
        assertTrue(authenticationRepository.isUserExists("a"));
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
    }
}
