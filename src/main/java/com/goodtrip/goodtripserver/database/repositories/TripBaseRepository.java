package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.CountryVisit;
import com.goodtrip.goodtripserver.database.models.Note;
import com.goodtrip.goodtripserver.database.models.TripState;
import com.goodtrip.goodtripserver.database.models.TripView;
import io.micrometer.common.lang.Nullable;

import java.sql.Date;
import java.util.List;

public interface TripBaseRepository {
    void saveTripAndWire(@jakarta.annotation.Nullable Integer tripId, Integer userId,
                         String title,
                         Integer moneyInUsd,
                         @Nullable String mainPhotoUrl,
                         Date departureDate,
                         Date arrivalDate,
                         TripState state,
                         List<Note> notes,
                         List<CountryVisit> countries);

    List<TripView> getAuthorsTrips(int userId, int startingNumber);
}
