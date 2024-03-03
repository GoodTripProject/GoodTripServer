package com.example.goodtripserver.authorization.service

import com.example.goodtripserver.authorization.model.AuthorizationRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthorizationServiceImpl : AuthorizationService {


    override fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any>{
        return ResponseEntity.ok().body("aboba")
        TODO("Not yet implemented")
    }

}