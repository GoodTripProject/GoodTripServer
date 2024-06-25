package com.goodtrip.goodtripserver.communication.route

import com.goodtrip.goodtripserver.communication.service.CommunicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/communication")
class CommunicationController {

    @Autowired
    private lateinit var communicationService: CommunicationService

    @PostMapping("/follow")
    suspend fun follow(@RequestParam userId: Int, @RequestParam author: String) = communicationService.follow(userId, author)


    @DeleteMapping("/unfollow")
    suspend fun unfollow(@RequestParam userId: Int, @RequestParam author: String) =
        communicationService.unfollow(userId, author)


    @PostMapping("/like")
    suspend fun like(@RequestParam userId: Int, @RequestParam tripId: Int) = communicationService.like(userId, tripId)


    @DeleteMapping("/delete_like")
    suspend fun deleteLike(@RequestParam userId: Int, @RequestParam tripId: Int) =
        communicationService.deleteLike(userId, tripId)

    @GetMapping("/followers")
    suspend fun getFollowers(@RequestParam userId: Int) = communicationService.getFollowers(userId)

    @GetMapping("/subscriptions")
    suspend fun getSubscriptions(@RequestParam userId: Int) = communicationService.getSubscriptions(userId)

}