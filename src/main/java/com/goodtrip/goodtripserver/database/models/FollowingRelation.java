package com.goodtrip.goodtripserver.database.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "followers", schema = "public", catalog = "GoodTripDatabase")
public class FollowingRelation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "author_id")
    private Integer authorId;
}
