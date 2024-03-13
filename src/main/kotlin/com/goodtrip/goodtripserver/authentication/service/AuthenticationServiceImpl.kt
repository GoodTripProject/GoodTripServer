package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.config.JwtService
import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    private lateinit var jwtService: JwtService

    override fun login(request: AuthorizationRequest): AuthenticationResponse {

        val user = authenticationRepository
            .login(request.username, request.password).get()//TODO заменить на нормальную ошибку
        val jwtToken = jwtService.generateToken(user)

        return AuthenticationResponse(
            handle = user.handle,
            token = jwtToken,
            name = user.name,
            surname = user.surname,
            url = null
        )
    }

    override fun register(request: RegisterRequest): AuthenticationResponse {
        //TODO хешировать пароль

        val user = User(request.username, request.handle, request.password, request.name, request.surname, "salt")
        val jwtToken = jwtService.generateToken(user)
        authenticationRepository.signUpIfNotExists(
            request.username,
            request.handle,
            request.password,
            request.name,
            request.surname,
            //TODO генерить соль
            "salt"
        )
        return AuthenticationResponse(
            handle = user.handle,
            token = jwtToken,
            name = user.name,
            surname = user.surname,
            url = null
        )

    }
}