package com.goodtrip.goodtripserver.authentication.repository

import com.goodtrip.goodtripserver.database.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

interface UserRepository : JpaRepository<User, Int> {
    //мб Integer
    fun findByEmail(email: String): Optional<User>
}