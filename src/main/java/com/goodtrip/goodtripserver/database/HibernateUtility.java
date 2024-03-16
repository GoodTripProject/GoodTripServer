package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.models.*;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

public class HibernateUtility {
    @Getter
    private static final SessionFactory sessionFactory;

    static {
        Properties connectionProperties = new Properties();
        try {
            connectionProperties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("hibernate.properties"));
        } catch (IOException e) {
            // TODO: Log
        }
        sessionFactory = new Configuration().mergeProperties(connectionProperties)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Trip.class)
                .addAnnotatedClass(Note.class)
                .addAnnotatedClass(CountryVisit.class)
                .addAnnotatedClass(CityVisit.class)
                .buildSessionFactory();
    }

}




