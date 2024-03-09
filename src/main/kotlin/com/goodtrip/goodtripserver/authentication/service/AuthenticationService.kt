package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import org.springframework.http.ResponseEntity


interface AuthenticationService /*: UserDetailsService*/ {
    fun login(request: AuthorizationRequest): ResponseEntity<AuthenticationResponse>
    fun register(request: RegisterRequest): ResponseEntity<AuthenticationResponse>
}