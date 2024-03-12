package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "country_visits", schema = "public", catalog = "GoodTripDatabase")
public class CountryVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="country")
    private String country;

    @OneToMany
    @JoinColumn(name = "country_visit_id")
    private List<CityVisit> cities;

    @Column(name="trip_id")
    private Integer tripId;
}
