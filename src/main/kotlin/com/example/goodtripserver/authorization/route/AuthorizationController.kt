package com.example.goodtripserver.authorization.route

import com.example.goodtripserver.authorization.model.AuthorizationRequest
import com.example.goodtripserver.authorization.service.AuthorizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthorizationController {

    @Autowired
    lateinit var authorizationService: AuthorizationService

    @ResponseBody
    @GetMapping("/authorize")
    fun authorize(@RequestBody authorizationRequest: AuthorizationRequest): ResponseEntity<Any> {
        return authorizationService.login(authorizationRequest)
    }

}