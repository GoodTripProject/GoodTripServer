package com.goodtrip.goodtripserver.database.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "notes", schema = "public", catalog = "GoodTripDatabase")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "google_place_id")
    private String googlePlaceId;

    @Column(name = "text")
    private String text;

    @Column(name = "trip_id")
    private Integer tripId;

    public Note(String title, @Nullable String photoUrl, String text, String googlePlaceId, Integer tripId) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.text = text;
        this.googlePlaceId = googlePlaceId;
        this.tripId = tripId;
    }

    public Note(String title, @Nullable String photoUrl, String googlePlaceId) {
        this.title = title;
        this.photoUrl = photoUrl;
        this.googlePlaceId = googlePlaceId;
    }

}
