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
    public void simpleLoginFailTest() {
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("a", "a"),
                Optional.empty());
        assertEquals(authenticationRepository.login("ab", "ab"),
                Optional.empty());
    }

    @Test
    public void simpleTest() {
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
        authenticationRepository.signUpifNotExists("a", "b", "c", "d", "e", "f", "g");
        Optional<User> user = authenticationRepository.login("a", "c");
        assertTrue(user.isPresent());
        Optional<String> salt = authenticationRepository.getSalt("a");
        if (salt.isEmpty()) {
            fail();
        }
        assertEquals("g", salt.get());
        authenticationRepository.updateToken("a", "c", "new_token");
        Optional<User> refreshedUser = authenticationRepository.login("a", "c");
        assertTrue(refreshedUser.isPresent());
        assertEquals("new_token", refreshedUser.get().getHashedToken());
        assertTrue(authenticationRepository.isUserExists("a"));
        authenticationRepository.deleteUserIfExists("a", "c");
        assertEquals(Optional.empty(), authenticationRepository.login("a", "c"));
    }

    @Test
    public void multipleSignUpTest() {
        authenticationRepository.deleteUserIfExists(
                "a", "c");
        assertTrue(authenticationRepository.signUpifNotExists(
                "a", "b", "c", "d", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpifNotExists(
                "a", "b", "c", "d", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpifNotExists(
                "a", "b", "c", "d", "e", "f", "g"));
        assertFalse(authenticationRepository.signUpifNotExists(
                "a", "b", "c", "d", "e", "f", "g"));
        authenticationRepository.deleteUserIfExists(
                "a", "c");
        assertEquals(Optional.empty(),
                authenticationRepository.login("a","c"));
    }
    @Test
    public void tokenLoginTest(){
        authenticationRepository.deleteUserIfExists(
                "a", "c");
        assertTrue(authenticationRepository.isTokenFree("d"));
        assertTrue(authenticationRepository.signUpifNotExists(
                "a", "b", "c", "d", "e", "f", "g"));
        assertFalse(authenticationRepository.isTokenFree("d"));
        assertTrue(authenticationRepository.loginUserWithToken("d").isPresent());
        authenticationRepository.deleteUserWithTokenIfExists("d");
    }
}
