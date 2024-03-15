package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.*;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface TripRepository {

    /**
     * Add trip to database and attach notes and visits (city to country, country to trip) to trip automatically.
     *
     * @param userId user id in database.
     * @param title title of trip.
     * @param moneyInUsd budget of trip.
     * @param mainPhotoUrl url to main photo.
     * @param departureDate date of departure.
     * @param arrivalDate date of arrival.
     * @param state state of trip.
     * @param notes notes to trip.
     * @param countries country visits.
     *
     */
    void addTrip(Integer userId, String title, Integer moneyInUsd,
                    @Nullable String mainPhotoUrl, Date departureDate, Date arrivalDate,
                    TripState state, List<Note> notes, List<CountryVisit> countries);

    void addNote(Integer tripId, Note note);

    boolean deleteNote(Integer noteId);

    boolean addCountryVisit(Integer tripId, CountryVisit countryVisit);

    boolean deleteTrip(int tripId);

    List<Trip> getTrips(int userId);

}
