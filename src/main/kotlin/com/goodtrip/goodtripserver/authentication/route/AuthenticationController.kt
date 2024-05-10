package com.goodtrip.goodtripserver.authentication.route

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.authentication.service.AuthenticationService
import lombok.RequiredArgsConstructor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthenticationController {
    private val logger: Logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @Autowired
    private lateinit var authenticationService: AuthenticationService

    @ResponseBody
    @PostMapping("/login")
    suspend fun authorize(@RequestBody authorizationRequest: AuthorizationRequest): ResponseEntity<AuthenticationResponse> {
        logger.info("authorizationRequest username:" + authorizationRequest.username + ", password:" + authorizationRequest.password)
        return ResponseEntity.ok(authenticationService.login(authorizationRequest))
    }

    @ResponseBody
    @PostMapping("/register")
    suspend fun register(@RequestBody registerRequest: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        logger.info("registerRequest username:" + registerRequest.username + ", password:" + registerRequest.password)
        return ResponseEntity.ok(authenticationService.register(registerRequest))
    }

}