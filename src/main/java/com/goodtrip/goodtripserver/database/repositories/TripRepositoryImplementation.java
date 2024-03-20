package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.*;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TripRepositoryImplementation implements TripRepository {
    private <T> Optional<T> getFirstIfExists(List<T> results) {
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.getFirst());
    }


    private Session getSession() {
        return null;
    }

    private void setCitiesVisits(@NotNull List<CountryVisit> countries, Session session) {
        for (CountryVisit visit : countries) {
            session.persist(visit);
            session.flush();
            for (CityVisit cityVisit : visit.getCities()) {
                cityVisit.setCountryVisitId(visit.getId());
                session.persist(cityVisit);
            }
        }
    }


    @Override
    public void addTrip(Integer userId,
                        String title,
                        Integer moneyInUsd,
                        @Nullable String mainPhotoUrl,
                        Date departureDate,
                        Date arrivalDate,
                        TripState state,
                        List<Note> notes,
                        List<CountryVisit> countries) {
        try (Session session = getSession()) {
            Trip newTrip = new Trip(userId, title, moneyInUsd, mainPhotoUrl, departureDate, arrivalDate, state, new ArrayList<>(), new ArrayList<>());
            Transaction transaction = session.beginTransaction();
            session.persist(newTrip);
            session.flush();
            for (Note note : notes) {
                note.setTripId(newTrip.getId());
                session.persist(note);
            }
            setCitiesVisits(countries, session);
            transaction.commit();
        }
    }

    @Override
    public void addNote(Integer tripId, Note note) {
        note.setTripId(tripId);
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(note);
            transaction.commit();
        }
    }

    public Optional<Note> getNoteById(int noteId) {
        try (Session session = getSession()) {
            TypedQuery<Note> query = session.createQuery("from Note m where m.id = :noteId", Note.class).setParameter("noteId", noteId);
            return getFirstIfExists(query.getResultList());
        }
    }

    @Override
    public boolean deleteNote(Integer noteId) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Note m where m.id = :noteId").setParameter("noteId", noteId);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        }
    }

    @Override
    public void addCountryVisit(Integer tripId, CountryVisit countryVisit) {
        countryVisit.setTripId(tripId);
        List<CityVisit> countryVisitCities = countryVisit.getCities();
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            countryVisit.setCities(Collections.emptyList());
            session.persist(countryVisit);
            countryVisit.setCities(countryVisitCities);
            setCitiesVisits(List.of(countryVisit), session);
            transaction.commit();
        }
    }

    public Optional<Trip> getTripById(int tripId) {
        try (Session session = getSession()) {
            TypedQuery<Trip> query = session.createQuery("from Trip m where m.id = :tripId", Trip.class).setParameter("tripId", tripId);
            return getFirstIfExists(query.getResultList());
        }
    }

    @Override
    public boolean deleteTrip(int tripId) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from Trip m where m.id = :tripId").setParameter("tripId", tripId);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        }
    }

    public List<Trip> getTrips(int userId) {
        try (Session session = getSession()) {
            TypedQuery<Trip> query = session.createQuery("from Trip m where m.userId = :userId", Trip.class).setParameter("userId", userId);
            return query.getResultList();
        }
    }

    @Override
    public boolean deleteCountryVisit(int countryVisitId) {
        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();
            MutationQuery query = session.createMutationQuery("delete from CountryVisit m where m.id = :countryVisitId").setParameter("countryVisitId", countryVisitId);
            int result = query.executeUpdate();
            transaction.commit();
            return result > 0;
        }
    }

}
