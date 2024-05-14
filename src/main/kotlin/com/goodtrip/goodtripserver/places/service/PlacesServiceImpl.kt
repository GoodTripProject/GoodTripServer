package com.goodtrip.goodtripserver.places.service

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.goodtrip.goodtripserver.places.model.City
import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.model.PlacesResponse
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

    private fun getUrl(placeRequest: PlaceRequest): String {
        val url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", placeRequest.location.replaceFirst('%', '?'))
            .queryParam("radius", placeRequest.radius.toString())
        if (!placeRequest.rankBy.isNullOrEmpty()) {
            url.queryParam("rankBy", placeRequest.rankBy)
        }
        placeRequest.type?.let {
            url.queryParam("rankBy", placeRequest.rankBy)
        }
        return url.queryParam("key", "API_KEY")//TODO вставить ключ
            .encode()
            .toUriString()
            .replace('?', '%')
            .replaceFirst('%', '?')
    }

    private fun getUrl(city: String) =
        UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/textsearch/json")
            .queryParam("query", city)
            .queryParam("type", "locality")
            .queryParam("key", "API_KEY")
            .build()

    private fun JsonNode.getPlace() = PlacesResponse(
        name = this["name"].toString(),
        lat = this["geometry"]["location"]["lat"].asDouble(),
        lng = this["geometry"]["location"]["lng"].asDouble(),
        icon = this["icon"].toString(),
        rating = this.get("rating")?.asInt() ?: 0,
        placeId = this.get("place_id").toString()//TODO потом проверить
    )

    override fun getNearPlaces(placeRequest: PlaceRequest): ResponseEntity<Any> {
        val url = getUrl(placeRequest)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == HttpStatus.OK.value()) {
            val places = mutableListOf<PlacesResponse>()
            val responseObject = objectMapper.readTree(response.body())
            if (responseObject["status"].toString() == "\"INVALID_REQUEST\"") {
                return ResponseEntity.badRequest().body("Invalid request")
            }
            responseObject["results"].forEach {
                val place = it.getPlace()
                places.add(place)
            }
            return ResponseEntity.ok(places)
        }
        return ResponseEntity.badRequest().body("Invalid request")
    }

    override fun getCoordinates(city: String): ResponseEntity<Any> {
        val url = getUrl(city)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(url.toUri())
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        if (response.statusCode() == HttpStatus.OK.value()) {
            val responseObject = objectMapper.readTree(response.body())
            if (responseObject["status"].toString() == "\"INVALID_REQUEST\"") {
                return ResponseEntity.badRequest().body("Invalid request")
            }

            return ResponseEntity.ok().body(
                City(
                    name = responseObject["results"][0]["formatted_address"].toString().drop(1).dropLast(1),
                    lat = responseObject["results"][0]["geometry"]["location"]["lat"].asDouble(),
                    lng = responseObject["results"][0]["geometry"]["location"]["lng"].asDouble()
                )
            )
        }
        return ResponseEntity.badRequest().body("Invalid request")
    }
}