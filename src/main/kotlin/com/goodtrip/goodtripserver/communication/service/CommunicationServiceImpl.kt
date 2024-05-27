package com.goodtrip.goodtripserver.communication.service

import com.goodtrip.goodtripserver.database.models.LikeRelation
import com.goodtrip.goodtripserver.database.repositories.FollowersRepository
import com.goodtrip.goodtripserver.database.repositories.LikeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CommunicationServiceImpl : CommunicationService {

    @Autowired
    private lateinit var followersRepository: FollowersRepository

    @Autowired
    private lateinit var likeRepository: LikeRepository


    override fun follow(userId: Int, author: String): ResponseEntity<String> {
        if (followersRepository.existsSubscription(userId, author)) {
            return ResponseEntity.badRequest().body("User is already subscribed")
        }
        followersRepository.followUserByHandles(userId, author)
        return ResponseEntity.ok().body("You are now subscribed to the user: $author")
    }

    override fun unfollow(userId: Int, author: String): ResponseEntity<String> {
        if (!followersRepository.existsSubscription(userId, author)) {
            return ResponseEntity.badRequest().body("User is already unsubscribed")
        }
        followersRepository.deleteSubscription(userId, author)
        return ResponseEntity.ok().body("You are no longer following the user: $author")
    }

    override fun like(userId: Int, tripId: Int): ResponseEntity<String> {
        if (likeRepository.existsByUserIdAndTripId(userId, tripId)) {
            return ResponseEntity.badRequest().body("Trip is already liked")
        }
        likeRepository.save(LikeRelation(tripId, userId))
        return ResponseEntity(HttpStatus.OK)
    }

    override fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String> {
        if (!likeRepository.existsByUserIdAndTripId(userId, tripId)) {
            return ResponseEntity.badRequest().body("Trip was not liked")
        }
        likeRepository.deleteByUserIdAndTripId(userId, tripId)
        return ResponseEntity(HttpStatus.OK)
    }

    override fun getFollowers(userId: Int) = ResponseEntity.ok().body(followersRepository.getAllUserFollowers(userId))


    override fun getSubscriptions(userId: Int) =
        ResponseEntity.ok().body(followersRepository.getAllUserSubscriptions(userId))
}