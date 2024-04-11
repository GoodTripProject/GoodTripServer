package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
@EnableTransactionManagement
@EnableAutoConfiguration
public class TripBaseRepositoryImpl implements TripBaseRepository {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional
    public void saveTripAndWire(Integer userId, String title, Integer moneyInUsd, String mainPhotoUrl, Date departureDate, Date arrivalDate, TripState state, List<Note> notes, List<CountryVisit> countries) {
        Trip newTrip = new Trip(userId, title, moneyInUsd, mainPhotoUrl, departureDate, arrivalDate, state, new ArrayList<>(), new ArrayList<>());
        manager.persist(newTrip);
        manager.flush();
        for (Note note : notes) {
            note.setTripId(newTrip.getId());
            manager.persist(note);
        }
        for (CountryVisit visit : countries) {
            visit.setTripId(newTrip.getId());
            manager.persist(visit);
            manager.flush();
            for (CityVisit cityVisit : visit.getCities()) {
                cityVisit.setCountryVisitId(visit.getId());
                manager.persist(cityVisit);
                manager.flush();
            }
        }
    }
}
