package com.goodtrip.goodtripserver.communication.route

import com.goodtrip.goodtripserver.communication.service.CommunicationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController()
@RequestMapping("/communication")
class CommunicationController {

    @Autowired
    private lateinit var communicationService: CommunicationService

    @PostMapping("/follow")
    fun follow(@RequestParam userId: Int, @RequestParam author: String) = communicationService.follow(userId, author)


    @DeleteMapping("/unfollow")
    fun unfollow(@RequestParam userId: Int, @RequestParam author: String) =
        communicationService.unfollow(userId, author)


    @PostMapping("/like")
    fun like(@RequestParam userId: Int, @RequestParam tripId: Int) = communicationService.like(userId, tripId)


    @DeleteMapping("/delete_like")
    fun deleteLike(@RequestParam userId: Int, @RequestParam tripId: Int) =
        communicationService.deleteLike(userId, tripId)

    @GetMapping("/followers")
    fun getFollowers(@RequestParam userId: Int) = communicationService.getFollowers(userId)

    @GetMapping("/subscriptions")
    fun getSubscriptions(@RequestParam userId: Int) = communicationService.getSubscriptions(userId)

}