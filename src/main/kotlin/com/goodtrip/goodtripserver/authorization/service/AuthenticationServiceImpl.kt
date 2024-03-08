package com.goodtrip.goodtripserver.authorization.service

import com.goodtrip.goodtripserver.authorization.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authorization.model.RegistrationRequest
import com.goodtrip.goodtripserver.authorization.model.UserDetailsImpl
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl : AuthenticationService {
    @Autowired
    lateinit var authenticationRepository: AuthenticationRepository

    override fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any> {
        TODO("Not yet implemented")
    }

    override fun register(registrationRequest: RegistrationRequest): ResponseEntity<String> {
        TODO("Not yet implemented")
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = authenticationRepository.getUserByEmail(username)
            .orElseThrow { UsernameNotFoundException("User with email = $username not exist!") }
        return UserDetailsImpl(user)
    }

}