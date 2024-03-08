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
    private lateinit var jwtService: JWTService
    private lateinit var authenticationManager: AuthenticationManager

    //TODO в общем надо будет разобраться с хешированием и jwt, закидывать в jwt хешированный пароль

    override fun login(authorizationRequest: AuthorizationRequest): ResponseEntity<Any> {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authorizationRequest.login,
                authorizationRequest.password
            )
        )// не знаю зачем
        try {
            val user = authenticationRepository.getUserByEmail(authorizationRequest.login).get()
            //TODO подумать, как сделать нормально без RegistrationRequest
            val jwt = jwtService.generateToken(
                RegistrationRequest(
                    username = user.username,
                    handle = user.handle,
                    password = user.password,
                    name = user.name,
                    surname = user.surname
                )
            )
            return ResponseEntity.ok(jwt)

        } catch (e: NoSuchElementException) {
            return ResponseEntity.badRequest().body("User doesn't exist")

        }

    }

    override fun register(registrationRequest: RegistrationRequest): ResponseEntity<String> {
        val jwt = JWTService().generateToken(registrationRequest)
        //TODO implement hashing
        val salt = ""
        if (authenticationRepository.signUpIfNotExists(
                registrationRequest.username,
                registrationRequest.handle,
                registrationRequest.password,
                jwt,
                registrationRequest.name,
                registrationRequest.surname,
                salt
            )
        ) {
            return ResponseEntity.ok(jwt)
        }
        return ResponseEntity.badRequest().body("User already exists")
    }


}