package com.goodtrip.goodtripserver.authentication.service

import com.goodtrip.goodtripserver.authentication.model.UrlHandler
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.coyote.BadRequestException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository

    override fun loadUserByUsername(email: String?) = authenticationRepository.getUserByUsername(email).get()


    override suspend fun updateUserPhoto(userId: Int, photoUrl: UrlHandler): ResponseEntity<String> {
        withContext(Dispatchers.IO) {
            authenticationRepository.updatePhotoUrlById(userId, photoUrl.url)
        }
        return ResponseEntity.ok("Photo updated")
    }

    override suspend fun getUserByHandle(handle: String) = try {
        ResponseEntity.ok().body(withContext(Dispatchers.IO) {
            authenticationRepository.getUserByHandle(handle)
        }.orElseThrow { BadRequestException() })
    } catch (e: BadRequestException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

}