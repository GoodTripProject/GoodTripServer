package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;


@Entity
@NoArgsConstructor
@Data
@Table(name = "city_visits", schema = "public", catalog = "GoodTripDatabase")
public class CityVisit {
    private final static int SRID = 4326;
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Getter
    @Column(name = "city")
    private String city;


    @Column(name = "point", columnDefinition = "geometry(Point," + SRID + ")")
    private Point point;

    @Setter
    @Getter
    @Column(name = "country_visit_id")
    private Integer countryVisitId;

    public CityVisit(String city, double lon, double lat) {
        this.city = city;
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), SRID);
        this.point = geometryFactory.createPoint(new Coordinate(lon, lat));
    }

    public double getLon() {
        return point.getX();
    }

    public double getLat() {
        return point.getY();
    }
}
