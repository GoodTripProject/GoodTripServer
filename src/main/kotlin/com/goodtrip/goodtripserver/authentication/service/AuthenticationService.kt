package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegistrationRequest
import org.springframework.http.ResponseEntity


interface AuthenticationService /*: UserDetailsService*/ {
    fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>
    fun register(registrationRequest: RegistrationRequest): ResponseEntity<String>
}