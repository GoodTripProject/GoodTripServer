package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.LikeRelation;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<LikeRelation,Integer> {
    void deleteByUserIdAndTripId(int userId, int tripId);
    boolean existsByUserIdAndTripId(int userId, int tripId);
}
