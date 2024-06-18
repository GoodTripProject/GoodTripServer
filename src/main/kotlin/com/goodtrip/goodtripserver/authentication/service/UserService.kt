package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.UrlHandler
import com.goodtrip.goodtripserver.database.models.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails

interface UserService {
    suspend fun loadUserByUsername(email: String?): UserDetails

    suspend fun updateUserPhoto(userId: Int, photoUrl: UrlHandler): ResponseEntity<String>

    suspend fun getUserByHandle(handle: String): ResponseEntity<User>

}
