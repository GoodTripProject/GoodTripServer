package com.goodtrip.goodtripserver.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripView {
    private Integer id;

    private String displayName;

    private String userMainPhotoUrl;

    private String title;

    private Integer moneyInUsd;

    private String mainPhotoUrl;

    private Date departureDate;

    private Date arrivalDate;

    private Timestamp publicationTimestamp;

    private TripState state;

    private List<CountryVisit> visits;

    public TripView(Trip trip, User user) {
        id = trip.getId();
        displayName = user.getName() + " " + user.getSurname();
        userMainPhotoUrl = user.getImageLink();
        title = trip.getTitle();
        visits = trip.getVisits();
        moneyInUsd = trip.getMoneyInUsd();
        mainPhotoUrl = trip.getMainPhotoUrl();
        departureDate = trip.getDepartureDate();
        arrivalDate = trip.getArrivalDate();
        publicationTimestamp = trip.getPublicationTimestamp();
        state = trip.getState();
    }

}
