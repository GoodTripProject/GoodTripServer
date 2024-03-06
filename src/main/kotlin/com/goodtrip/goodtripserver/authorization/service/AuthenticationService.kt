package com.goodtrip.goodtripserver.authorization.service

import com.goodtrip.goodtripserver.authorization.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authorization.model.RegistrationRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService


interface AuthenticationService : UserDetailsService {
    fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>
    fun register(registrationRequest: RegistrationRequest): ResponseEntity<String>
}