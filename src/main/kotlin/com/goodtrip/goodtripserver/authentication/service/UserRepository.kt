package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Int> {
    //мб Integer
    fun findByEmail(email: String): Optional<User>
}