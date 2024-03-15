package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.CountryVisit;
import com.goodtrip.goodtripserver.database.models.Note;
import com.goodtrip.goodtripserver.database.models.Trip;
import com.goodtrip.goodtripserver.database.models.TripState;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository {

    /**
     * Add trip to database and attach notes and visits (city to country, country to trip) to trip automatically.
     *
     * @param userId        user id in database.
     * @param title         title of trip.
     * @param moneyInUsd    budget of trip.
     * @param mainPhotoUrl  url to main photo.
     * @param departureDate date of departure.
     * @param arrivalDate   date of arrival.
     * @param state         state of trip.
     * @param notes         notes to trip.
     * @param countries     country visits.
     */
    void addTrip(Integer userId, String title, Integer moneyInUsd,
                 @Nullable String mainPhotoUrl, Date departureDate, Date arrivalDate,
                 TripState state, List<Note> notes, List<CountryVisit> countries);

    /**
     * Add note to trip.
     *
     * @param tripId id of trip.
     * @param note   note.
     */
    void addNote(Integer tripId, Note note);

    /**
     * Delete note from trip.
     *
     * @param noteId id of note.
     * @return true if note exists, false otherwise.
     */
    boolean deleteNote(Integer noteId);

    /**
     * Add country visit to trip.
     *
     * @param tripId       id of trip.
     * @param countryVisit country visit.
     *
     */
    void addCountryVisit(Integer tripId, CountryVisit countryVisit);

    /**
     * Delete trip.
     *
     * @param tripId id of note.
     * @return true if trip exists, false otherwise.
     */
    boolean deleteTrip(int tripId);

    /**
     * Get all trips of user.
     *
     * @param userId id of user.
     * @return list of trips.
     */
    List<Trip> getTrips(int userId);

    boolean deleteCountryVisit(int countryVisitId);

    Optional<Trip> getTripById(int tripId);

    Optional<Note> getNoteById(int noteId);

}
