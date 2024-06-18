package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.config.JwtService
import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.database.models.FollowingRelation
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import com.goodtrip.goodtripserver.database.repositories.FollowersRepository
import com.goodtrip.goodtripserver.encrypting.PasswordHasher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.net.URI


@Service
@RequiredArgsConstructor
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var hasher: PasswordHasher

    @Autowired
    private lateinit var followersRepository: FollowersRepository

    override suspend fun login(request: AuthorizationRequest): AuthenticationResponse {
        val salt = withContext(Dispatchers.IO) {
            authenticationRepository.getSalt(request.username)
        }.get()
        val user = withContext(Dispatchers.IO) {
            authenticationRepository
                .findUserByUsernameAndHashedPassword(request.username, hasher.hashPassword(request.password, salt))
        }.get()//TODO заменить на нормальную ошибку
        val jwtToken = jwtService.generateToken(user)

        return AuthenticationResponse(
            id = user.id,
            handle = user.handle,
            token = jwtToken,
            name = user.name,
            surname = user.surname,
            url = if (user.imageLink == null) null else URI.create(user.imageLink).toURL()
        )
    }

    override suspend fun register(request: RegisterRequest): AuthenticationResponse {
        if (withContext(Dispatchers.IO) {
                authenticationRepository.existsUserByUsername(request.username)
            }) {
            throw BadCredentialsException("Username ${request.username} already exists")
        }
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
        val userWithId = withContext(Dispatchers.IO) {
            authenticationRepository.save(
                User(
                    request.username,
                    request.handle,
                    hashedPassword,
                    request.name,
                    request.surname,
                    salt
                )
            )
        }
        withContext(Dispatchers.IO) {
            followersRepository.save(
                FollowingRelation(userWithId.id, userWithId.id)
            )
        }
        return AuthenticationResponse(
            id = userWithId.id,
            handle = userWithId.handle,
            token = jwtToken,
            name = userWithId.name,
            surname = userWithId.surname,
            url = null
        )

    }
}