package com.goodtrip.goodtripserver.user

import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun getUserTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange("/user?handle=2j7", HttpMethod.GET, httpEntity, User::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body?.id).isEqualTo(378)
        assertThat(body?.username).isEqualTo("FLtCPU@gmail.com")
        assertThat(body?.handle).isEqualTo("2j7")
        assertThat(body?.imageLink).isNull()
        assertThat(body?.name).isEqualTo("rFaaba5Em")
        assertThat(body?.surname).isEqualTo("s2Z1ivN14Wasd7c")
    }

    @Test
    fun getUserWithoutTokenTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange("/user?handle=2j7", HttpMethod.GET, httpEntity, User::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    fun getNotExistUserTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            "/user?handle=${Utils.getRandomString(20)}",
            HttpMethod.GET,
            httpEntity,
            User::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }


}