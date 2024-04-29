package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.Trip;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends CrudRepository<Trip, Integer>, TripBaseRepository {
    Integer deleteTripById(int tripId);

    /**
     * Get all trips of user.
     *
     * @param userId id of user.
     * @return list of trips.
     */
    List<Trip> getTripsByUserIdOrderByPublicationTimestampDesc(int userId);


    /**
     * Get trip by id.
     *
     * @param tripId id of trip.
     * @return Optional.of if country visit exists, Optional.empty() otherwise.
     */
    Optional<Trip> getTripById(int tripId);
}
