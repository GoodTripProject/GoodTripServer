package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.UrlHandler
import com.goodtrip.goodtripserver.database.models.User
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails

interface UserService {
    fun loadUserByUsername(email: String?): UserDetails

    fun updateUserPhoto(userId: Int, photoUrl: UrlHandler): ResponseEntity<String>

    fun getUserByHandle(handle: String): ResponseEntity<User>

}
