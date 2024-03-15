package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.HibernateUtility;
import com.goodtrip.goodtripserver.database.models.*;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

import javax.lang.model.type.NullType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class TripRepositoryImplementation implements TripRepository {
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
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
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

    private static void setCitiesVisits(@NotNull List<CountryVisit> countries, Session session) {
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
    public void addNote(Integer tripId, Note note) {
        note.setTripId(tripId);
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(note);
            transaction.commit();
        }
    }

    public Optional<Note> getNoteById(int noteId) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<Note> query = session.createQuery(
                            "from Note m where m.id = :noteId", Note.class)
                    .setParameter("noteId", noteId);
            List<Note> notes = query.getResultList();
            if (notes.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(notes.getFirst());
        }
    }
    @Override
    public boolean deleteNote(Integer noteId) {
        Optional<Note> noteOptional = getNoteById(noteId);
        if (noteOptional.isEmpty()) {
            return false;
        }
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(noteOptional.get());
            transaction.commit();
        }
        return true;
    }

    @Override
    public boolean addCountryVisit(Integer tripId, CountryVisit countryVisit) {
        Optional<Trip> tripOptional = getTripById(tripId);
        if (tripOptional.isEmpty()) {
            return false;
        }
        countryVisit.setTripId(tripId);
        List<CityVisit> countryVisitCities = countryVisit.getCities();
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            countryVisit.setCities(Collections.emptyList());
            session.persist(countryVisit);
            countryVisit.setCities(countryVisitCities);
            setCitiesVisits(List.of(countryVisit),session);
            transaction.commit();
        }
        return true;
    }

    public Optional<Trip> getTripById(int tripId) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<Trip> query = session.createQuery(
                            "from Trip m where m.id = :tripId", Trip.class)
                    .setParameter("tripId", tripId);
            List<Trip> trips = query.getResultList();
            if (trips.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(trips.getFirst());
        }
    }

    @Override
    public boolean deleteTrip(int tripId) {
        Optional<Trip> tripOptional = getTripById(tripId);
        if (tripOptional.isEmpty()) {
            return false;
        }
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(tripOptional.get());
            transaction.commit();
        }
        return true;
    }

    public List<Trip> getTrips(int userId){
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            TypedQuery<Trip> query = session.createQuery(
                            "from Trip m where m.userId = :userId", Trip.class)
                    .setParameter("userId", userId);
            return query.getResultList();
        }
    }

    @Override
    public boolean deleteCountryVisit(int countryVisitId) {
        try (Session session = HibernateUtility.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            MutationQuery query = session.createMutationQuery(
                            "delete from CountryVisit m where m.id = :countryVisitId")
                    .setParameter("countryVisitId", countryVisitId);
            int result = query.executeUpdate();
            if (result == 0){
                return false;
            }
            transaction.commit();
        }
        return true;
    }

}
