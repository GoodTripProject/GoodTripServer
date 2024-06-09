package com.goodtrip.goodtripserver.user

import com.goodtrip.goodtripserver.authentication.model.UrlHandler
import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import kotlin.random.Random


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

    @Test
    fun updatePhotoTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, handle, id) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val url = UrlHandler(Utils.getRandomString(20))
        val httpEntity = HttpEntity(url, headers)
        val response = restTemplate.exchange(
            "/auth/update_photo?userId=$id",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val response2 = restTemplate.exchange(
            "/user?handle=$handle",
            HttpMethod.GET,
            httpEntity,
            User::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response2.body?.imageLink).isEqualTo(url.url)
    }

    @Test
    fun updatePhotoWithoutTokenTest() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val url = UrlHandler(Utils.getRandomString(20))
        val httpEntity = HttpEntity(url, headers)
        val response = restTemplate.exchange(
            "/auth/update_photo?userId=${Random.nextInt()}",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)

    }


}