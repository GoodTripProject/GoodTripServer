package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.models.User;
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
        Optional<User> user = authenticationRepository.login("a", "c");
        assertTrue(user.isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());
        authenticationRepository.updateToken("a","c","new_token");
        Optional<User> refreshedUser = authenticationRepository.login("a", "c");
        assertTrue(refreshedUser.isPresent());
        assertEquals("new_token",refreshedUser.get().getHashedToken());
        assertTrue(authenticationRepository.isUserExists("a"));
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
    }
}
