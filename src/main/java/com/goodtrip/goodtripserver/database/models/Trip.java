package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "trips", schema = "public", catalog = "GoodTripDatabase")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_id")
    private Integer userId;

    @Column(name="title")
    private String title;

    @Column(name="money_in_usd")
    private Integer moneyInUsd;

    @Column(name="main_photo_url")
    private String mainPhotoUrl;

    @Column(name = "departure_date", nullable = false)
    @Temporal(TemporalType.DATE)
    @CreatedDate
    private Date departureDate;

    @Column(name = "arrival_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date arrivalDate;

    @Column(name = "publication_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Timestamp publicationTimestamp;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "state")
    private TripState state;

    @OneToMany
    @JoinColumn(name = "trip_id")
    private List<Note> notes;

    @OneToMany
    @JoinColumn(name = "trip_id")
    private List<CountryVisit> visits;

}
