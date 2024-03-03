package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.HibernateUtility;
import com.goodtrip.goodtripserver.database.models.User;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;


public class AuthenticationRepository {

    public Optional<String> getSalt(String username) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            Predicate usernameEquals = builder.equal(root.get("username"), username);
            TypedQuery<User> query = session.createQuery(criteriaQuery.select(root).where(usernameEquals));
            List<User> results = query.getResultList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(results.getFirst().getSalt());
        }
    }

    public Optional<User> login(String username, String password) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);
            Predicate usernameEquals = builder.equal(root.get("username"), username);
            Predicate passwordEquals = builder.equal(root.get("hashedPassword"), password);
            TypedQuery<User> query = session.createQuery(criteriaQuery.select(root).where(usernameEquals).where(passwordEquals));
            List<User> results = query.getResultList();
            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(results.getFirst());
        }
    }

    public void signUp(String username, String handle, String hashedPassword,
                       String hashedToken, String name, String surname, String salt) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(new User(username, handle, hashedPassword, hashedToken, name, surname, salt));
            transaction.commit();
        }
    }

    public void deleteUserIfExists(String username, String password) {
        Optional<User> user = login(username, password);
        if (user.isEmpty()) {
            return;
        }
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(user.get());
            transaction.commit();
        }
    }

    public boolean isUserExists(String username) {
        return getSalt(username).isPresent();
    }

    public void updateToken(String username,String hashedPassword,String hashedToken){
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    "update User m set m.hashedToken = :hashedToken " +
                            " where m.username = :username" +
                            " and m.hashedPassword = :hashedPassword"
                    ,null).setParameter("hashedToken", hashedToken)
                    .setParameter("username",username)
                    .setParameter("hashedPassword",hashedPassword);
            Transaction transaction = session.beginTransaction();
            query.executeUpdate();
            transaction.commit();
        }
    }

}
