package com.goodtrip.goodtripserver.communication.service

import com.goodtrip.goodtripserver.database.models.LikeRelation
import com.goodtrip.goodtripserver.database.repositories.FollowersRepository
import com.goodtrip.goodtripserver.database.repositories.LikeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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


    override suspend fun follow(userId: Int, author: String): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                followersRepository.existsSubscription(userId, author)
            }) {
            return ResponseEntity.badRequest().body("User is already subscribed")
        }
        withContext(Dispatchers.IO) {
            followersRepository.followUserByHandles(userId, author)
        }
        return ResponseEntity.ok().body("You are now subscribed to the user: $author")
    }

    override suspend fun unfollow(userId: Int, author: String): ResponseEntity<String> {
        if (!withContext(Dispatchers.IO) {
                followersRepository.existsSubscription(userId, author)
            }) {
            return ResponseEntity.badRequest().body("User is already unsubscribed")
        }
        withContext(Dispatchers.IO) {
            followersRepository.deleteSubscription(userId, author)
        }
        return ResponseEntity.ok().body("You are no longer following the user: $author")
    }

    override suspend fun like(userId: Int, tripId: Int): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                likeRepository.existsByUserIdAndTripId(userId, tripId)
            }) {
            return ResponseEntity.badRequest().body("Trip is already liked")
        }
        withContext(Dispatchers.IO) {
            likeRepository.save(LikeRelation(tripId, userId))
        }
        return ResponseEntity(HttpStatus.OK)
    }

    override suspend fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String> {
        if (!withContext(Dispatchers.IO) {
                likeRepository.existsByUserIdAndTripId(userId, tripId)
            }) {
            return ResponseEntity.badRequest().body("Trip was not liked")
        }
        withContext(Dispatchers.IO) {
            likeRepository.deleteByUserIdAndTripId(userId, tripId)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    override suspend fun getFollowers(userId: Int) = ResponseEntity.ok().body(withContext(Dispatchers.IO) {
        followersRepository.getAllUserFollowers(userId)
    })


    override suspend fun getSubscriptions(userId: Int) =
        ResponseEntity.ok().body(withContext(Dispatchers.IO) {
            followersRepository.getAllUserSubscriptions(userId)
        })
}