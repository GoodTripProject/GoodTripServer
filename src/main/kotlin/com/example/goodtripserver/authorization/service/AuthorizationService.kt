package com.example.goodtripserver.authorization.service

import com.example.goodtripserver.authorization.model.AuthorizationRequest
import org.springframework.http.ResponseEntity


interface AuthorizationService {
    fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>
}