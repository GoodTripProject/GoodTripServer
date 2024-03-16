package com.goodtrip.goodtripserver.encrypting;


import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.UUID;

@Component
public class PasswordHasherImplementation implements PasswordHasher {
    private final String secretSalt;
    private MessageDigest digest;

    {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException exception) {
            //TODO log
        }
    }

    public PasswordHasherImplementation() {
        Properties saltProperties = new Properties();
        try {
            saltProperties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("encryption.properties"));
        } catch (IOException e) {
            // TODO: Log
        }
        secretSalt = saltProperties.getProperty("salt");
    }

    public PasswordHasherImplementation(String secretSalt) {
        this.secretSalt = secretSalt;
    }

    @Override
    public String getPersonalSalt() {
        return UUID.randomUUID().toString();
    }

    private String getHash(String string) {
        byte[] hash = digest.digest(string.getBytes(StandardCharsets.UTF_8));
        StringBuilder resultHash = new StringBuilder();
        for (byte currentByte : hash) {
            resultHash.append(String.format("%02x", currentByte));
        }
        return resultHash.toString();
    }

    @Override
    public String hashPassword(String password, String personalSalt) {
        String passwordWithSalt = personalSalt + secretSalt + password;
        return getHash(getHash(passwordWithSalt));
    }
}
