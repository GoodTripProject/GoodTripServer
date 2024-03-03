package com.example.goodtripserver.authorization.service

import com.example.goodtripserver.authorization.model.AuthorizationRequest
import com.example.goodtripserver.authorization.model.RegistrationRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl : AuthenticationService {

    override fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>{
        return ResponseEntity.ok().body("aboba")
        TODO("Not yet implemented")
    }

    override fun register(registrationRequest: RegistrationRequest): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

}