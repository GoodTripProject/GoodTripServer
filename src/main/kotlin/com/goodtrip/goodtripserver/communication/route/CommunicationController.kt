package com.goodtrip.goodtripserver.communication.route

import com.goodtrip.goodtripserver.communication.service.CommunicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("/communication")
class CommunicationController {

    @Autowired
    private lateinit var communicationService: CommunicationService

    @PostMapping("/follow")
    fun follow(@RequestParam userId: Int, @RequestParam author: String): ResponseEntity<String> {
        return communicationService.follow(userId, author)
    }

    @PostMapping("/unfollow")
    fun unfollow(@RequestParam userId: Int, @RequestParam author: String): ResponseEntity<String> {
        return communicationService.unfollow(userId, author)
    }

    @PostMapping("/like")
    fun like(@RequestParam userId: Int, @RequestParam tripId: Int): ResponseEntity<String> {
        return communicationService.like(userId, tripId)
    }

    @PostMapping("/delete_like")
    fun deleteLike(@RequestParam userId: Int, @RequestParam tripId: Int): ResponseEntity<String> {
        return communicationService.deleteLike(userId, tripId)
    }

}