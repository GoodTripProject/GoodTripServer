package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository of authentication.
 */
@EnableTransactionManagement
public interface AuthenticationRepository extends CrudRepository<User, Integer> {
    @Query("SELECT salt from User where username = ?1")
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
    boolean existsUserByUsername(String username);

    Optional<User> getUserByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE User u  SET u.imageLink= :photoUrl where u.id= :id")
    void updatePhotoUrlById(Integer id, String photoUrl);

    Optional<User> getUserByHandle(String handle);
}
