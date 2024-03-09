package com.goodtrip.goodtripserver.encrypting;

public interface PasswordHasher {
    /**
     * Get personal salt of user.
     *
     * @return random string, which uses like personal salt of user.
     */
    String getPersonalSalt();
    /**
     * Hash password to save in database.
     *
     * @param password     password of user.
     * @param personalSalt personal salt of user.
     * @return hashed password.
     */
    String hashPassword(String password, String personalSalt);
}
