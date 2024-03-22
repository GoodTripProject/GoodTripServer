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
     * @param name           name
     * @param surname        surname
     * @param salt           salt to save his password
     * @return true if user added, false if user existed or token is not unique
     */
    boolean signUpIfNotExists(String username, String handle, String hashedPassword, String name, String surname, String salt);

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
     * Return user by Email
     *
     * @param email user email
     * @return Optional.empty() if user doesn't exist, User otherwise
     */
    Optional<User> getUserByEmail(String email);

}
