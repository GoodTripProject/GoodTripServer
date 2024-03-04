package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.HibernateUtility;
import com.goodtrip.goodtripserver.database.models.User;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Repository of authentication implementation.
 */
@Repository
public class AuthenticationRepositoryImplementation implements AuthenticationRepository {

    private <T> Optional<T> getFirstIfExists(List<T> results){
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.getFirst());
    }
    public Optional<String> getSalt(String username) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<String> query = session.createQuery(
                            "select m.salt from User m where m.username = :username", String.class)
                    .setParameter("username", username);
            return getFirstIfExists(query.getResultList());
        }
    }


    public Optional<User> login(String username, String hashedPassword) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<User> query = session.createQuery(
                            "from User m where m.username = :username" +
                                    " and m.hashedPassword = :hashedPassword", User.class)
                    .setParameter("username", username)
                    .setParameter("hashedPassword", hashedPassword);
            return getFirstIfExists(query.getResultList());
        }
    }

    public boolean signUpIfNotExists(String username, String handle, String hashedPassword,
                                     String hashedToken, String name, String surname, String salt) {
        if (isUserExists(username) || !isTokenFree(hashedToken)) {
            return false;
        }
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(new User(username, handle, hashedPassword, hashedToken, name, surname, salt));
            transaction.commit();
            return true;
        }
    }

    /**
     * Delete user.
     *
     * @param user User
     */
    private void deleteUser(User user) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        }
    }


    public void deleteUserIfExists(String username, String hashedPassword) {
        Optional<User> user = login(username, hashedPassword);
        if (user.isEmpty()) {
            return;
        }
        deleteUser(user.get());
    }


    public boolean isUserExists(String username) {
        return getSalt(username).isPresent();
    }


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


    public Optional<User> loginUserWithToken(String hashedToken) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<User> query = session.createQuery(
                            "from User m where m.hashedToken = :hashedToken", User.class)
                    .setParameter("hashedToken", hashedToken);
            return getFirstIfExists(query.getResultList());
        }
    }


    public boolean isTokenFree(String hashedToken) {
        return loginUserWithToken(hashedToken).isEmpty();
    }


    public void deleteUserWithTokenIfExists(String hashedToken) {
        Optional<User> user = loginUserWithToken(hashedToken);
        if (user.isEmpty()) {
            return;
        }
        deleteUser(user.get());
    }

}
