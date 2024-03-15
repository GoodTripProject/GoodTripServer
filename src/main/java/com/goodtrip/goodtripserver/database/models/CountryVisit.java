package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "country_visits", schema = "public", catalog = "GoodTripDatabase")
public class CountryVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "country")
    private String country;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
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
