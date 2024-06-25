package com.goodtrip.goodtripserver.communication.service

import com.goodtrip.goodtripserver.database.models.User
import org.springframework.http.ResponseEntity

interface CommunicationService {
    suspend fun follow(userId: Int, author: String): ResponseEntity<String>

    suspend fun unfollow(userId: Int, author: String): ResponseEntity<String>

    suspend fun like(userId: Int, tripId: Int): ResponseEntity<String>

    suspend fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String>

    suspend fun getFollowers(userId: Int): ResponseEntity<List<User>>

    suspend fun getSubscriptions(userId: Int): ResponseEntity<List<User>>

}
