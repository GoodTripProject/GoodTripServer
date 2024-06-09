package com.goodtrip.goodtripserver.communication

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
    }
}