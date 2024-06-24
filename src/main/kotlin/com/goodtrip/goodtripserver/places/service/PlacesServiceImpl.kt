package com.goodtrip.goodtripserver.places.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.model.PlacesResponse
import com.goodtrip.goodtripserver.trip.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class PlacesServiceImpl : PlacesService {

    private val objectMapper = jacksonObjectMapper()

    @Autowired
    private lateinit var environment: Environment

    private fun getUrl(placeRequest: PlaceRequest): String {
        val url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", "${placeRequest.lat}?2C${placeRequest.lng}")
            .queryParam("radius", placeRequest.radius.toString())
        placeRequest.type?.let {
            url.queryParam("rankBy", placeRequest.rankBy)
        }
        return url.queryParam("key", environment.getProperty("PLACES_API_KEY"))
            .encode()
            .toUriString()
            .replace('?', '%')
            .replaceFirst('%', '?')
    }

    private fun String.dropQuotes() = this.drop(1).dropLast(1)

    private fun getUrl(city: String) =
        UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
            .queryParam("query", city)
            .queryParam("type", "locality")
            .queryParam("key", environment.getProperty("PLACES_API_KEY"))
            .build()

    private fun getUrl(width: String, height: String, photoReference: String) =
        UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/photo")
            .queryParam("maxwidth", width)
            .queryParam("maxheight", height)
            .queryParam("photo_reference", photoReference)
            .queryParam("key", environment.getProperty("PLACES_API_KEY"))
            .build()


    private suspend fun getPhoto(node: JsonNode): String {
        val photo = node.get("photos")?.get(0) ?: return ""
        val url = getUrl(
            width = photo.get("width").toString(),
            height = photo.get("height").toString(),
            photoReference = photo.get("photo_reference").toString().dropQuotes()
        )
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(url.toUri())
            .build()
        val response =
            withContext(Dispatchers.IO) {
                client.send(request, HttpResponse.BodyHandlers.ofString())
            }

        return Jsoup.parse(response.body()).select("A")[0].attribute("href").value
    }

    private suspend fun JsonNode.getPlace() = PlacesResponse(
        name = this["name"].toString().dropQuotes(),
        lat = this["geometry"]["location"]["lat"].asDouble(),
        lng = this["geometry"]["location"]["lng"].asDouble(),
        photo = getPhoto(this),
        rating = this.get("rating")?.asInt() ?: 0,
        placeId = this.get("place_id").toString().dropQuotes()
    )

    override suspend fun getNearPlaces(placeRequest: PlaceRequest): ResponseEntity<Any> {
        val url = getUrl(placeRequest)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response = withContext(Dispatchers.IO) {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        }

        if (response.statusCode() == HttpStatus.OK.value()) {
            val places = mutableListOf<PlacesResponse>()
            val responseObject = objectMapper.readTree(response.body())
            if (responseObject["status"].toString().dropQuotes() == "INVALID_REQUEST") {
                return ResponseEntity.badRequest().body("Invalid request")
            }
            responseObject["results"].forEach {
                if (!it["types"].map { type -> type.asText() }.contains("political")) {
                    val place = it.getPlace()

                    places.add(place)
                }
            }
            return ResponseEntity.ok(places)
        }
        return ResponseEntity.badRequest().body("Invalid request")
    }

    override suspend fun getCoordinates(city: String): ResponseEntity<Any> {
        val url = getUrl(city)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(url.toUri())
            .build()
        val response = withContext(Dispatchers.IO) {
            client.send(request, HttpResponse.BodyHandlers.ofString())
        }

        if (response.statusCode() == HttpStatus.OK.value()) {
            val responseObject = objectMapper.readTree(response.body())
            if (responseObject["status"].toString().dropQuotes() == "INVALID_REQUEST") {
                return ResponseEntity.badRequest().body("Invalid request")
            }

            return try {
                ResponseEntity.ok().body(
                    City(
                        city = responseObject["results"][0]["formatted_address"].toString().dropQuotes(),
                        latitude = responseObject["results"][0]["geometry"]["location"]["lat"].asDouble(),
                        longitude = responseObject["results"][0]["geometry"]["location"]["lng"].asDouble()
                    )
                )
            } catch (e: NullPointerException) {
                return ResponseEntity(HttpStatus.BAD_REQUEST)
            }
        }
        return ResponseEntity.badRequest().body("Invalid request")
    }
}