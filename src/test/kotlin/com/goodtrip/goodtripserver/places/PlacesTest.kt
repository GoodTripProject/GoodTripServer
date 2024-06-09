package com.goodtrip.goodtripserver.places

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.model.PlacesResponse
import com.goodtrip.goodtripserver.trip.model.City
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.*
import kotlin.random.Random

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlacesTest {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate


    @Test
    fun nearPlacesTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(requestHeaders, restTemplate)
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
            restTemplate.exchange("/places", HttpMethod.POST, httpEntity, Array<PlacesResponse>::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body?.size == 18)
        val place = body?.get(0)
        assertThat(place?.placeId).isEqualTo("ChIJFfyzTTeuEmsRuMxvFyNRfbk")
        assertThat(place?.name).isEqualTo("ibis Sydney Darling Harbour")
        assertThat(place?.lat).isEqualTo(-33.8705877)
        assertThat(place?.lng).isEqualTo(151.1979415)
        assertThat(place?.rating).isEqualTo(3)
        assertThat(place?.photo).isEqualTo("https://lh3.googleusercontent.com/places/ANXAkqEgbVk7TZXUnVxdB0XDMdqWZTlX-tsM0QZfQH0m5tDOp9PuLIM08M14-Utf-6k1kLwn6djLM4oceRHyQk_IU9zvt9Fe7R_9RGE=s1600-w6720-h4480")
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

    @Test
    fun nearPlacesIncorrectRequestTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(requestHeaders, restTemplate)
        requestHeaders.add("Authorization", "Bearer $token")
        val placeRequest = PlaceRequest(
            lng = Random.nextDouble(1000.0, 10000.0),
            lat = Random.nextDouble(1000.0, 10000.0),
            radius = Random.nextInt(10000, 100000),
            rankBy = null,
            type = null
        )
        val httpEntity = HttpEntity(placeRequest, requestHeaders)
        val response =
            restTemplate.exchange(
                "/places",
                HttpMethod.POST,
                httpEntity,
                String::class.java
            )
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body).isEqualTo("Invalid request")
    }

    @Test
    fun getCoordinatesTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(requestHeaders, restTemplate)
        requestHeaders.add("Authorization", "Bearer $token")
        val httpEntity = HttpEntity("", requestHeaders)
        val response = restTemplate.exchange("/coordinates?city=Moscow", HttpMethod.GET, httpEntity, City::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        val body = response.body
        assertThat(body?.city).isEqualTo("Moscow, Russia")
        assertThat(body?.longitude).isEqualTo(37.6172999)
        assertThat(body?.latitude).isEqualTo(55.755826)

        val response2 = restTemplate.exchange("/coordinates?city=Vladivostok", HttpMethod.GET, httpEntity, City::class.java)
        assertThat(response2.statusCode).isEqualTo(HttpStatus.OK)
        val body2 = response2.body
        assertThat(body2?.city).isEqualTo("Vladivostok, Primorsky Krai, Russia")
        assertThat(body2?.longitude).isEqualTo(131.9112975)
        assertThat(body2?.latitude).isEqualTo(43.1332484)
    }

    @Test
    fun getCoordinatesBadRequestTest() {
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val token = Utils.getToken(requestHeaders, restTemplate)
        requestHeaders.add("Authorization", "Bearer $token")
        val httpEntity = HttpEntity("", requestHeaders)
        val response = restTemplate.exchange("/coordinates?city=M3123124", HttpMethod.GET, httpEntity, City::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    fun getCoordinatesWithoutToken(){
        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        val httpEntity = HttpEntity("", requestHeaders)
        val response = restTemplate.exchange("/coordinates?city=Vladivostok", HttpMethod.GET, httpEntity, City::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

}
