package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.HibernateUtility;
import com.goodtrip.goodtripserver.database.models.User;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;


/**
 * Repository of authentication.
 */
public class AuthenticationRepository {

    /**
     * Get salt for user.
     *
     * @param username username (usually email)
     * @return Optional.empty() if user does not exist, salt of user otherwise.
     */
    public Optional<String> getSalt(String username) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<String> query = session.createQuery(
                            "select m.salt from User m where m.username = :username", String.class)
                    .setParameter("username", username);
            List<String> results = query.getResultList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(results.getFirst());
        }
    }

    /**
     * Login user.
     *
     * @param username       username(usually email)
     * @param hashedPassword hashed password
     * @return Optional.empty() if user have incorrect password or username, user otherwise
     */
    public Optional<User> login(String username, String hashedPassword) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<User> query = session.createQuery(
                            "from User m where m.username = :username" +
                                    " and m.hashedPassword = :hashedPassword", User.class)
                    .setParameter("username", username)
                    .setParameter("hashedPassword", hashedPassword);
            List<User> results = query.getResultList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(results.getFirst());
        }
    }

    /**
     * Creates new user and add him to database.
     *
     * @param username       username (usually email)
     * @param handle         handle of user
     * @param hashedPassword hashed password
     * @param hashedToken    hashed token
     * @param name           name
     * @param surname        surname
     * @param salt           salt to save his password
     */
    public void signUp(String username, String handle, String hashedPassword,
                       String hashedToken, String name, String surname, String salt) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(new User(username, handle, hashedPassword, hashedToken, name, surname, salt));
            transaction.commit();
        }
    }

    /**
     * Delete user from database if user exists.
     *
     * @param username       username (usually email).
     * @param hashedPassword hashed password of user.
     */
    public void deleteUserIfExists(String username, String hashedPassword) {
        Optional<User> user = login(username, hashedPassword);
        if (user.isEmpty()) {
            return;
        }
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(user.get());
            transaction.commit();
        }
    }

    /**
     * Checks if user exists in database.
     *
     * @param username username (usually email)
     * @return true if user exists in database, false otherwise
     */
    public boolean isUserExists(String username) {
        return getSalt(username).isPresent();
    }


    /**
     * Update token of user if user exists.
     *
     * @param username       username - usually email.
     * @param hashedPassword - hashed password of user.
     * @param hashedToken    - new hashed token.
     */
    public void updateToken(String username, String hashedPassword, String hashedToken) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                            "update User m set m.hashedToken = :hashedToken " +
                                    " where m.username = :username" +
                                    " and m.hashedPassword = :hashedPassword"
                            , null).setParameter("hashedToken", hashedToken)
                    .setParameter("username", username)
                    .setParameter("hashedPassword", hashedPassword);
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }

}
