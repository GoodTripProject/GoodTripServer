package com.goodtrip.goodtripserver.database;

import com.goodtrip.goodtripserver.database.configs.DatabaseConfig;
import com.goodtrip.goodtripserver.database.models.LikeRelation;
import com.goodtrip.goodtripserver.database.models.Trip;
import com.goodtrip.goodtripserver.database.models.TripState;
import com.goodtrip.goodtripserver.database.models.User;
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository;
import com.goodtrip.goodtripserver.database.repositories.LikeRepository;
import com.goodtrip.goodtripserver.database.repositories.TripRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = DatabaseConfig.class)
@ComponentScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EntityScan(basePackages = {"com.goodtrip.goodtripserver.database.models.*"})
@EnableJpaRepositories("com.goodtrip.goodtripserver.database.repositories")
@EnableAutoConfiguration(exclude = {JdbcTemplateAutoConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LikeTests {
    @Autowired
    private AuthenticationRepository authenticationRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private LikeRepository repository;
    private User user;
    private Trip trip;

    private User createUser() {
        String username = UUID.randomUUID().toString().substring(0, 31);
        String password = UUID.randomUUID().toString().substring(0, 31);
        return authenticationRepository.save(new User(username, UUID.randomUUID().toString().substring(0, 31),
                password, "a", "b", ""));
    }

    @BeforeAll
    public void setAuthenticationRepositoryAndMakeTrip() {
        user = createUser();
        tripRepository.saveTripAndWire(null, user.getId(), "1", 2, "aa",
                new Date(0), new Date(1), TripState.IN_PROCESS, Collections.emptyList(), Collections.emptyList());
        trip = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(user.getId()).getFirst();
    }

    @Test
    @Transactional
    public void like_LikeIsNotExist_LikeIsDeleted() {
        repository.save(new LikeRelation(trip.getId(), user.getId()));
        assertTrue(repository.existsByUserIdAndTripId(user.getId(), trip.getId()));
        repository.deleteByUserIdAndTripId(user.getId(), trip.getId());
        assertFalse(repository.existsByUserIdAndTripId(user.getId(), trip.getId()));
    }

    @AfterAll
    public void deleteTripsAndUsers() {
        tripRepository.delete(trip);
        authenticationRepository.delete(user);
    }
}
