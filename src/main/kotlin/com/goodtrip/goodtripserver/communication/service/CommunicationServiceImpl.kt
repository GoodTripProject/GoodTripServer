package com.goodtrip.goodtripserver.communication.service

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class CommunicationServiceImpl : CommunicationService {
    override fun follow(userId: Int, author: String): ResponseEntity<String> {
        if (false /* пользователь уже подписан*/) {
            return ResponseEntity.badRequest().body("User is already subscribed")
        }
        return ResponseEntity.ok().body("You are now subscribed to the user: $author")
    }

    override fun unfollow(userId: Int, author: String): ResponseEntity<String> {
        if (false /* пользователь не подписан*/) {
            return ResponseEntity.badRequest().body("User is already unsubscribed")
        }
        return ResponseEntity.ok().body("You are no longer following the user: $author")
    }

    override fun like(userId: Int, tripId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    override fun deleteLike(userId: Int, tripId: Int): ResponseEntity<String> {
        TODO("Not yet implemented")
    }
}