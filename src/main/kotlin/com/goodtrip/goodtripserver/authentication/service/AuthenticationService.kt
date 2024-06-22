package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest


interface AuthenticationService {

    suspend fun login(request: AuthorizationRequest): AuthenticationResponse

    suspend fun register(request: RegisterRequest): AuthenticationResponse

}