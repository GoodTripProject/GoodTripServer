package com.goodtrip.goodtripserver.user.route

import com.goodtrip.goodtripserver.authentication.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @ResponseBody
    @GetMapping("/user")
    suspend fun getUser(@RequestParam handle: String) = userService.getUserByHandle(handle)
}