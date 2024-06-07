package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@EnableTransactionManagement
@EnableAutoConfiguration
public class TripBaseRepositoryImpl implements TripBaseRepository {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Override
    @Transactional
    public void saveTripAndWire(@Nullable Integer tripId, Integer userId, String title, Integer moneyInUsd, String mainPhotoUrl, Date departureDate, Date arrivalDate, TripState state, List<Note> notes, List<CountryVisit> countries) {
        Trip newTrip = new Trip(userId, title, moneyInUsd, mainPhotoUrl, departureDate, arrivalDate, state, new ArrayList<>(), new ArrayList<>());
        newTrip.setId(tripId);
        manager.merge(newTrip);
        manager.flush();
        for (Note note : notes) {
            note.setTripId(newTrip.getId());
            manager.merge(note);
        }
        for (CountryVisit visit : countries) {
            visit.setTripId(newTrip.getId());
            manager.merge(visit);
            manager.flush();
            for (CityVisit cityVisit : visit.getCities()) {
                cityVisit.setCountryVisitId(visit.getId());
                manager.merge(cityVisit);
                manager.flush();
            }
        }
    }

    @Override
    @Transactional
    public List<TripView> getAuthorsTrips(int userId, int startingNumber) {
        return manager.createQuery("SELECT trip FROM Trip trip, FollowingRelation relation " +
                        "WHERE relation.userId = :userId " +
                        "AND relation.authorId = trip.userId " +
                        "AND trip.state = 2" +
                        "ORDER BY trip.publicationTimestamp DESC ", Trip.class)
                .setParameter("userId", userId)
                .setFirstResult(startingNumber)
                .setMaxResults(10)
                .getResultStream()
                .map((trip) -> {
                    Optional<User> user = authenticationRepository.findById(userId);
                    return user.map(value -> new TripView(trip, value)).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<Trip> getTripsOfSpecificUser(int authorId) {
        return manager.createQuery("SELECT trip FROM Trip trip " +
                        "WHERE trip.userId = :authorId " +
                        "AND trip.state = 2" +
                        "ORDER BY trip.publicationTimestamp DESC ", Trip.class)
                .setParameter("authorId", authorId).getResultList();
    }
}
