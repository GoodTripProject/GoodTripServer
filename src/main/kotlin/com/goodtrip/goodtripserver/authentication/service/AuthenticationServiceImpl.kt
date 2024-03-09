package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegistrationRequest
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import java.util.NoSuchElementException


@Service
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository
    //TODO в общем надо будет разобраться с хешированием и jwt, закидывать в jwt хешированный пароль

    override fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any> {
        TODO()

    }

    override fun register(registrationRequest: RegistrationRequest): ResponseEntity<String> {
        TODO()

    }
}