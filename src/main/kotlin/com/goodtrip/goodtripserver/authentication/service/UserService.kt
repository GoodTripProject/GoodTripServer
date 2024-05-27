package com.goodtrip.goodtripserver.authentication.service

import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails

interface UserService {
    fun loadUserByUsername(email: String?): UserDetails

    fun updateUserPhoto(userId: Int, photoUrl: String): ResponseEntity<String>

}
