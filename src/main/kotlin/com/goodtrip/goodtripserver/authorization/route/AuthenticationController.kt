package com.goodtrip.goodtripserver.authorization.route

import com.goodtrip.goodtripserver.authorization.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authorization.model.RegistrationRequest
import com.goodtrip.goodtripserver.authorization.service.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @ResponseBody
    @GetMapping("/login")
    fun authorize(@RequestBody authorizationRequest: AuthorizationRequest): ResponseEntity<Any> {
        return authenticationService.login(authorizationRequest)
    }

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody registrationRequest: RegistrationRequest): ResponseEntity<String> {
        return authenticationService.register(registrationRequest)
    }

}