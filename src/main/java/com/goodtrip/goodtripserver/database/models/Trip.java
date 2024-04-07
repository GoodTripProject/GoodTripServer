package com.goodtrip.goodtripserver.database.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "trips", schema = "public", catalog = "GoodTripDatabase")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "title")
    private String title;

    @Column(name = "money_in_usd")
    private Integer moneyInUsd;

    @Column(name = "main_photo_url")
    @Nullable
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

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id")
    private List<Note> notes;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "trip_id")
    private List<CountryVisit> visits;

    public Trip(Integer userId, String title, Integer moneyInUsd, @Nullable String mainPhotoUrl, Date departureDate, Date arrivalDate, TripState state, List<Note> notes, List<CountryVisit> visits) {
        this.userId = userId;
        this.title = title;
        this.moneyInUsd = moneyInUsd;
        this.mainPhotoUrl = mainPhotoUrl;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.state = state;
        this.notes = notes;
        this.visits = visits;
    }
}
