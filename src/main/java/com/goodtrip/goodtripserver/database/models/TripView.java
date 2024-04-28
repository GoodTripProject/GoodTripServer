package com.goodtrip.goodtripserver.database.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;

@Data
public class TripView {
    private Integer id;

    private Integer userId;

    private String title;

    private Integer moneyInUsd;

    private String mainPhotoUrl;

    private Date departureDate;


    private Date arrivalDate;


    private Timestamp publicationTimestamp;

    private TripState state;
    public TripView(Trip trip){
        id = trip.getId();
        userId = trip.getUserId();
        title  = trip.getTitle();
        moneyInUsd = trip.getMoneyInUsd();
        mainPhotoUrl = trip.getMainPhotoUrl();
        departureDate = trip.getDepartureDate();
        arrivalDate = trip.getArrivalDate();
        publicationTimestamp = trip.getPublicationTimestamp();
        state = trip.getState();
    }

}
