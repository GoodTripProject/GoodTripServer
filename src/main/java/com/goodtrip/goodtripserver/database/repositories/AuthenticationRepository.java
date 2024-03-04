package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.User;

import java.util.Optional;

/**
 * Repository of authentication.
 */
public interface AuthenticationRepository {
    /**
     * Get salt for user.
     *
     * @param username username (usually email)
     * @return Optional.empty() if user does not exist, salt of user otherwise.
     */
    Optional<String> getSalt(String username);

    /**
     * Login user.
     *
     * @param username       username(usually email)
     * @param hashedPassword hashed password
     * @return Optional.empty() if user have incorrect password or username, user otherwise
     */
    Optional<User> login(String username, String hashedPassword);

    /**
     * Creates new user and add him to database (only if user with this login did not exist and token is unique).
     *
     * @param username       username (usually email)
     * @param handle         handle of user
     * @param hashedPassword hashed password
     * @param hashedToken    hashed token
     * @param name           name
     * @param surname        surname
     * @param salt           salt to save his password
     * @return true if user added, false if user existed or token is not unique
     */
    boolean signUpifNotExists(String username, String handle, String hashedPassword,
                              String hashedToken, String name, String surname, String salt);

    /**
     * Delete user from database if user exists.
     *
     * @param username       username (usually email).
     * @param hashedPassword hashed password of user.
     */
    void deleteUserIfExists(String username, String hashedPassword);

    /**
     * Checks if user exists in database.
     *
     * @param username username (usually email)
     * @return true if user exists in database, false otherwise
     */
    boolean isUserExists(String username);

    /**
     * Update token of user if user exists.
     *
     * @param username       username - usually email.
     * @param hashedPassword - hashed password of user.
     * @param hashedToken    - new hashed token.
     */
    void updateToken(String username, String hashedPassword, String hashedToken);

    /**
     * Login user with token.
     *
     * @param hashedToken token of user.
     * @return Optional.empty() if token is not valid, User otherwise
     */
    Optional<User> loginUserWithToken(String hashedToken);

    /**
     * Checks if token is free.
     *
     * @param hashedToken token
     * @return true if token is free, false otherwise
     */
    boolean isTokenFree(String hashedToken);

    /**
     * Deletes user with token only if user exists.
     *
     * @param hashedToken token of user
     */
    void deleteUserWithTokenIfExists(String hashedToken);
}
