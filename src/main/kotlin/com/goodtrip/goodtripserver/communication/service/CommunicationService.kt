package com.goodtrip.goodtripserver.communication.service

import org.springframework.http.ResponseEntity

interface CommunicationService {
    fun follow(userId: Int, author: String): ResponseEntity<String>

    fun unfollow(userId: Int, author: String): ResponseEntity<String>

    fun like(userId: Int, tripId: Int): ResponseEntity<String>

    fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String>

}
