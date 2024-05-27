package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    override fun loadUserByUsername(email: String?): UserDetails {
        return authenticationRepository.getUserByUsername(email).get()
    }

    override fun updateUserPhoto(userId: Int, photoUrl: String): ResponseEntity<String> {
        authenticationRepository.updatePhotoUrlById(userId, photoUrl)
        return ResponseEntity.ok("Photo updated")
    }
}