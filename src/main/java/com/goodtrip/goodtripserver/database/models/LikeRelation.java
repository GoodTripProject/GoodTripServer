package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "like_relation", schema = "public", catalog = "GoodTripDatabase")
public class LikeRelation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "trip_id")
    private Integer tripId;
    @Column(name = "user_id")
    private Integer userId;

    public LikeRelation(Integer tripId, Integer userId) {
        this.tripId = tripId;
        this.userId = userId;
    }
}
