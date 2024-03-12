package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.config.JwtService
import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.authentication.repository.UserRepository
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

//    @Autowired
//    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    override fun login(request: AuthorizationRequest): AuthenticationResponse {
        //TODO поменять на метод андрея
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.login,
                request.password
            )
        )
        val user = authenticationRepository.getUserByEmail(request.login).orElseThrow()
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

        val user = User(request.username, request.handle, request.password," ", request.name, request.surname, "salt")
        val jwtToken = jwtService.generateToken(user)
        authenticationRepository.signUpIfNotExists(
            request.username,
            request.handle,
            request.password,
            jwtToken,
            request.name,
            request.surname,
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