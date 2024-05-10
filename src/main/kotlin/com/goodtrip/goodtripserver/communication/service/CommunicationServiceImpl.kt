package com.goodtrip.goodtripserver.communication.service

import org.springframework.http.ResponseEntity

class CommunicationServiceImpl : CommunicationService {
    override fun follow(userId: Int, authorId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    override fun unfollow(userId: Int, authorId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    override fun like(userId: Int, tripId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    override fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }
}