package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest


interface AuthenticationService /*: UserDetailsService*/ {
    fun login(request: AuthorizationRequest): AuthenticationResponse
    fun register(request: RegisterRequest): AuthenticationResponse
}