package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository of authentication.
 */
public interface AuthenticationRepository extends CrudRepository<User,Integer> {
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
    Optional<User> findUserByUsernameAndHashedPassword(String username, String hashedPassword);

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
    boolean addUserIfNotExists(String username, String handle, String hashedPassword, String name, String surname, String salt);

    /**
     * Delete user from database if user exists.
     *
     * @param username       username (usually email).
     * @param hashedPassword hashed password of user.
     */
    void deleteUserIfExistsByUsernameAndHashedPassword(String username, String hashedPassword);

    /**
     * Checks if user exists in database.
     *
     * @param username username (usually email)
     * @return true if user exists in database, false otherwise
     */
    boolean existsUserBy(String username);
}
