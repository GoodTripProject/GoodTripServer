package com.goodtrip.goodtripserver.authentication

import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.AuthorizationRequest
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import kotlin.random.Random
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    @Test
    fun registerTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val handle = getRandomString(Random.nextInt(20))
        val name = getRandomString(Random.nextInt(20))
        val surname = getRandomString(Random.nextInt(20))
        val registerRequest = RegisterRequest(
            username = "${getRandomString(Random.nextInt(20))}@gmail.com",
            handle = handle,
            password = "test",
            name = name,
            surname = surname
        )
        val httpEntity = HttpEntity(registerRequest, requestHeaders)
        val response =
            restTemplate.exchange("/auth/register", HttpMethod.POST, httpEntity, AuthenticationResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.handle).isEqualTo(handle)
        assertThat(response.body?.name).isEqualTo(name)
        assertThat(response.body?.surname).isEqualTo(surname)
        assertThat(response.body?.token).isNotBlank()
    }

    @Test
    fun doubleRegisterTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val registerRequest = RegisterRequest(
            username = "${getRandomString(Random.nextInt(20))}@gmail.com",
            handle = getRandomString(Random.nextInt(20)),
            password = "test",
            name = getRandomString(Random.nextInt(20)),
            surname = getRandomString(Random.nextInt(20))
        )
        val httpEntity = HttpEntity(registerRequest, requestHeaders)
        val response =
            restTemplate.exchange("/auth/register", HttpMethod.POST, httpEntity, AuthenticationResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val response2 =
            restTemplate.exchange("/auth/register", HttpMethod.POST, httpEntity, AuthenticationResponse::class.java)
        assertThat(response2.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun loginTest() {
        val username = getRandomString(Random.nextInt(20))
        val password = getRandomString(Random.nextInt(20))
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val registerRequest = RegisterRequest(
            username = username,
            handle = getRandomString(Random.nextInt(20)),
            password = password,
            name = getRandomString(Random.nextInt(20)),
            surname = getRandomString(Random.nextInt(20))
        )
        val registerHttpEntity = HttpEntity(registerRequest, requestHeaders)
        restTemplate.exchange("/auth/register", HttpMethod.POST, registerHttpEntity, AuthenticationResponse::class.java)
        val authorizationRequest = AuthorizationRequest(
            username = username,
            password = password
        )
        val loginHttpEntity = HttpEntity(authorizationRequest, requestHeaders)
        val response =
            restTemplate.exchange("/auth/login", HttpMethod.POST, loginHttpEntity, AuthenticationResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.token).isNotBlank()
    }

    @Test
    fun noSuchUserLogin() {
        val username = getRandomString(Random.nextInt(20))
        val password = getRandomString(Random.nextInt(20))
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val authorizationRequest = AuthorizationRequest(
            username = username,
            password = password
        )
        val loginHttpEntity = HttpEntity(authorizationRequest, requestHeaders)
        val response =
            restTemplate.exchange("/auth/login", HttpMethod.POST, loginHttpEntity, AuthenticationResponse::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

}