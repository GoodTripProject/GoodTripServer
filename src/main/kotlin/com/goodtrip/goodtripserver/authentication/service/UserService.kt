package com.goodtrip.goodtripserver.authentication.service

import org.springframework.security.core.userdetails.UserDetails

interface UserService {
    fun loadUserByUsername(email: String?): UserDetails
}