package com.goodtrip.goodtripserver.places


import com.goodtrip.goodtripserver.authentication.model.AuthenticationResponse
import com.goodtrip.goodtripserver.authentication.model.RegisterRequest
import com.goodtrip.goodtripserver.places.model.PlaceRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlacesTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    private fun getRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }


    @Test
    fun nearPlacesTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val registerRequest = RegisterRequest(
            username = "${getRandomString(20)}.com",
            password = getRandomString(10),
            handle = getRandomString(10),
            name = getRandomString(15),
            surname = getRandomString(15),
        )
        val loginHttpEntity = HttpEntity(registerRequest, requestHeaders)
        val token =
            restTemplate.exchange(
                "/auth/register",
                HttpMethod.POST,
                loginHttpEntity,
                AuthenticationResponse::class.java
            )
                .body?.token
        requestHeaders.add("Authorization", "Bearer $token")
        val placeRequest = PlaceRequest(
            lng = 151.1957362,
            lat = -33.8670522,
            radius = 500,
            rankBy = null,
            type = null
        )
        val httpEntity = HttpEntity(placeRequest, requestHeaders)
        val response =
            restTemplate.exchange("/places", HttpMethod.POST, httpEntity, List::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body?.size == 18)
//        val place = body?.get(0)
//        assertThat(place?.get("placeId")).isEqualTo("ChIJFfyzTTeuEmsRuMxvFyNRfbk")
//        "name": "ibis Sydney Darling Harbour",
//        "lat": -33.8705877,
//        "lng": 151.1979415,
//        "photo": "https://lh3.googleusercontent.com/places/ANXAkqEgbVk7TZXUnVxdB0XDMdqWZTlX-tsM0QZfQH0m5tDOp9PuLIM08M14-Utf-6k1kLwn6djLM4oceRHyQk_IU9zvt9Fe7R_9RGE=s1600-w6720-h4480",
//        "rating": 3,
//        "placeId": "ChIJFfyzTTeuEmsRuMxvFyNRfbk"
    }

    @Test
    fun nearPlacesWithoutTokenTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val placeRequest = PlaceRequest(
            lng = 151.1957362,
            lat = -33.8670522,
            radius = 500,
            rankBy = null,
            type = null
        )
        val httpEntity = HttpEntity(placeRequest, requestHeaders)
        val response =
            restTemplate.exchange("/places", HttpMethod.POST, httpEntity, List::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

}
