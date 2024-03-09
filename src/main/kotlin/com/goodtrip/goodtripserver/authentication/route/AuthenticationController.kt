package com.goodtrip.goodtripserver.authentication.route

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegistrationRequest
import com.goodtrip.goodtripserver.authentication.service.AuthenticationService
import lombok.RequiredArgsConstructor
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
@RequiredArgsConstructor
class AuthenticationController {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @ResponseBody
    @GetMapping("/login")
    fun authorize(@RequestBody authorizationRequest: AuthorizationRequest): ResponseEntity<AuthorizationRequest> {
        return authenticationService.login(authorizationRequest)
    }

    @ResponseBody
    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegistrationRequest): ResponseEntity<AuthenticationResponse> {
        return authenticationService.register(registerRequest)
    }

}