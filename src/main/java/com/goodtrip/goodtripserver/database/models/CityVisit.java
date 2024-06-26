package com.goodtrip.goodtripserver.database.models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

import java.io.IOException;
import java.io.Serializable;


@Entity
@NoArgsConstructor
@Data
@Table(name = "city_visits", schema = "public", catalog = "GoodTripDatabase")
public class CityVisit implements Serializable {
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
    @JsonSerialize(using = PointCustomSerializer.class)
    @JsonDeserialize(using = PointCustomDeserializer.class)
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


    static class PointCustomSerializer extends StdSerializer<Point> {
        protected PointCustomSerializer() {
            super(Point.class, true);
        }

        @Override
        public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("x", point.getX());
            jsonGenerator.writeNumberField("y", point.getY());
            jsonGenerator.writeEndObject();
        }
    }

    static class PointCustomDeserializer extends StdDeserializer<Point> {
        protected PointCustomDeserializer() {
            super(Point.class);
        }

        @Override
        public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                throws IOException {
            JsonNode node = jsonParser.getCodec().readTree(jsonParser);
            Double x = (Double) node.get("x").numberValue();
            Double y = (Double) node.get("y").numberValue();
            return new Point(new CoordinateArraySequence(new Coordinate[]{new Coordinate(x, y)}),
                    new GeometryFactory(new PrecisionModel(), SRID));
        }
    }
}
