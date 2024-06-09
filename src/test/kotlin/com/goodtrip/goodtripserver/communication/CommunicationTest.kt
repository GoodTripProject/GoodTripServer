package com.goodtrip.goodtripserver.communication

import com.goodtrip.goodtripserver.database.models.User
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CommunicationTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun followTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, handle, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, id2) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val response2 = restTemplate.exchange(
            "/communication/follow?userId=$id2&author=$handle",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun doubleFollowingTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, _, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, _) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val response2 = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun unfollowTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, _, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, _) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val response2 = restTemplate.exchange(
            "/communication/unfollow?userId=$id&author=$handle2",
            HttpMethod.DELETE,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)
    }

    @Test
    fun strangeUnfollowTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, handle, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, id2) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)
        val response1 = restTemplate.exchange(
            "/communication/unfollow?userId=$id&author=$handle2",
            HttpMethod.DELETE,
            httpEntity,
            String::class.java
        )
        assertThat(response1.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)

        val response2 = restTemplate.exchange(
            "/communication/unfollow?userId=$id2&author=$handle",
            HttpMethod.DELETE,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun getFollowersTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, _, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, id2) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)

        val response1 = restTemplate.exchange(
            "/communication/followers?userId=$id2",
            HttpMethod.GET,
            httpEntity,
            Array<User>::class.java
        )
        assertThat(response1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response1.body?.size).isEqualTo(1)

        val response2 = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)

        val response3 = restTemplate.exchange(
            "/communication/followers?userId=$id2",
            HttpMethod.GET,
            httpEntity,
            Array<User>::class.java
        )
        assertThat(response3.statusCode).isEqualTo(HttpStatus.OK)
        val body = response3.body
        assertThat(body?.size).isEqualTo(2)
        assertThat(body?.first()?.id).isEqualTo(id)
    }


    @Test
    fun getSubscriptionsTest(){
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val (token, _, id) = Utils.`get token, handle and id`(headers, restTemplate)
        val (_, handle2, id2) = Utils.`get token, handle and id`(headers, restTemplate)
        headers.setBearerAuth(token)
        val httpEntity = HttpEntity(null, headers)

        val response1 = restTemplate.exchange(
            "/communication/subscriptions?userId=$id",
            HttpMethod.GET,
            httpEntity,
            Array<User>::class.java
        )
        assertThat(response1.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response1.body?.size).isEqualTo(1)

        val response2 = restTemplate.exchange(
            "/communication/follow?userId=$id&author=$handle2",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)

        val response3 = restTemplate.exchange(
            "/communication/subscriptions?userId=$id",
            HttpMethod.GET,
            httpEntity,
            Array<User>::class.java
        )
        assertThat(response3.statusCode).isEqualTo(HttpStatus.OK)
        val body = response3.body
        assertThat(body?.size).isEqualTo(2)
        assertThat(body?.get(1)?.id).isEqualTo(id2)
    }


}