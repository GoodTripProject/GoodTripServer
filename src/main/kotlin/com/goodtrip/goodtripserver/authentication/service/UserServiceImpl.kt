package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepositoryImplementation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {
//    @Autowired
    private val authenticationRepository = AuthenticationRepositoryImplementation()//TODO понять в чем трабл

    override fun loadUserByUsername(email: String?): UserDetails {
        return authenticationRepository.getUserByEmail(email).get()
    }
}