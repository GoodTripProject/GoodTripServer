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

    public FollowingRelation(Integer userId, Integer authorId) {
        this.userId = userId;
        this.authorId = authorId;
    }
}
