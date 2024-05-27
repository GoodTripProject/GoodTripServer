package com.goodtrip.goodtripserver.database.repositories;

import com.goodtrip.goodtripserver.database.models.FollowingRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
public interface FollowersRepository extends CrudRepository<FollowingRelation, Integer> {
    @Modifying
    @Query(value = "INSERT INTO followers (user_id, author_id) " +
            "SELECT :userId,(SELECT id FROM users WHERE handle = :authorHandle);",
            nativeQuery = true)
    void followUserByHandles(Integer userId, String authorHandle);

    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM FollowingRelation WHERE userId = :userId " +
            "AND authorId = (SELECT id FROM User WHERE handle = :authorHandle)) THEN true ELSE false END")
    boolean existsSubscription(Integer userId, String authorHandle);

    @Modifying
    @Query("DELETE FROM FollowingRelation WHERE userId = :userId " +
            "AND authorId = (SELECT id FROM User WHERE handle = :authorHandle)")
    void deleteSubscription(Integer userId, String authorHandle);

}
