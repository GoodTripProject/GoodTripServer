package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.config.JwtService
import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import com.goodtrip.goodtripserver.encrypting.PasswordHasherImplementation
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
    private val hasher = PasswordHasherImplementation()

    override fun login(request: AuthorizationRequest): AuthenticationResponse {
        val salt = authenticationRepository.getSalt(request.username).get()
        val user = authenticationRepository
            .login(request.username, hasher.hashPassword(request.password, salt))
            .get()//TODO заменить на нормальную ошибку
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
        val salt = hasher.personalSalt
        val hashedPassword = hasher.hashPassword(request.password, salt)
        val user = User(
            request.username,
            request.handle,
            hashedPassword,
            request.name,
            request.surname,
            salt
        )
        val jwtToken = jwtService.generateToken(user)
        authenticationRepository.signUpIfNotExists(
            request.username,
            request.handle,
            hashedPassword,
            request.name,
            request.surname,
            salt
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