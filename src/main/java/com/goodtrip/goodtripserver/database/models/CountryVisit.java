package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "country_visits", schema = "public", catalog = "GoodTripDatabase")
public class CountryVisit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country")
    private String country;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_visit_id")
    private List<CityVisit> cities;

    @Column(name = "trip_id")
    private Integer tripId;

    public CountryVisit(String country, List<CityVisit> cities, Integer tripId) {
        this.country = country;
        this.cities = cities;
        this.tripId = tripId;
    }

    public CountryVisit(String country, List<CityVisit> cities) {
        this.country = country;
        this.cities = cities;
    }

}