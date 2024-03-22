package com.goodtrip.goodtripserver.encrypting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherImplementationTests {

    PasswordHasher passwordHasher = new PasswordHasherImplementation("salt");

    @Test
    void getPersonalSalt_NoSalt_SaltsAreRandom() {
        assertNotEquals(passwordHasher.getPersonalSalt(), passwordHasher.getPersonalSalt());
    }

    @Test
    void hashPassword_PasswordIsNotHashed_PasswordHashedSeveralTimesAndResultIsSame() {
        String hash = passwordHasher.hashPassword("qwerty", "salt");
        for (int index = 0; index < 10; ++index) {
            assertEquals(hash, passwordHasher.hashPassword("qwerty", "salt"));
        }
    }

    @Test
    void hashPassword_PasswordAndSaltAreEmpty_PasswordHasherUsesDoubleSha256Algorithm() {
        String hash = passwordHasher.hashPassword("", "");
        assertEquals("9641ca7fe349aaf28fdca8179a8851f2dc2a467a3c75edff08005f0953d1d1d5", hash);
    }
}