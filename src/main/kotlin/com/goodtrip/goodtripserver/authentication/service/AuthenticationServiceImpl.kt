package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.authentication.repository.UserRepository
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    private lateinit var userRepository: UserRepository
    //TODO в общем надо будет разобраться с хешированием и jwt, закидывать в jwt хешированный пароль

    override fun login(request: AuthorizationRequest): ResponseEntity<AuthenticationResponse> {
        TODO()
    //        return ResponseEntity.ok(authenticationRepository.login())

    }

    override fun register(request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        val user = User.builder().username(request.username).handle()
            .build()
        TODO()

    }
}