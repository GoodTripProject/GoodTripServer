package com.goodtrip.goodtripserver.communication.route

import com.goodtrip.goodtripserver.communication.service.CommunicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController("communication")
class CommunicationController {

    @Autowired
    private lateinit var communicationService: CommunicationService

    @PostMapping("/follow")
    fun follow(@RequestParam userId: Int, @RequestParam authorId: Int): ResponseEntity<String> {
        return communicationService.follow(userId, authorId)
    }

}