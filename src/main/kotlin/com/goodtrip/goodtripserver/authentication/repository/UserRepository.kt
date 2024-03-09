package com.goodtrip.goodtripserver.authentication.repository

import com.goodtrip.goodtripserver.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Int> {
    //мб Integer
    fun findByUsername(email: String): Optional<User>//TODO глянуть не Email ли
}