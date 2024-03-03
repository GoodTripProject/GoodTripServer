package com.example.goodtripserver.authorization.service

import com.example.goodtripserver.authorization.model.AuthorizationRequest
import com.example.goodtripserver.authorization.model.RegistrationRequest
import org.springframework.http.ResponseEntity


interface AuthenticationService {
    fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>
    fun register(registrationRequest: RegistrationRequest) : ResponseEntity<String>
}