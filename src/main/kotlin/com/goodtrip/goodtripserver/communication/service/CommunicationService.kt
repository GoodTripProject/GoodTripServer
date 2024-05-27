package com.goodtrip.goodtripserver.communication.service

import com.goodtrip.goodtripserver.database.models.User
import org.springframework.http.ResponseEntity

interface CommunicationService {
    fun follow(userId: Int, author: String): ResponseEntity<String>

    fun unfollow(userId: Int, author: String): ResponseEntity<String>

    fun like(userId: Int, tripId: Int): ResponseEntity<String>

    fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String>

    fun getFollowers(userId: Int): ResponseEntity<List<User>>

    fun getSubscriptions(userId: Int): ResponseEntity<List<User>>

}
