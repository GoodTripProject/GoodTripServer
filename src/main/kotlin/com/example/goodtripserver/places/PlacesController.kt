package com.example.goodtripserver.places

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.http.*
import org.springframework.http.*
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.net.URI
import java.net.http.HttpRequest

@RestController
class PlacesController {

    private final val objectMapper = jacksonObjectMapper()

    private fun getUrl(placeRequest: PlaceRequest): String {
        val url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", placeRequest.location.replaceFirst('%', '?'))//smth here
            .queryParam("radius", placeRequest.radius.toString())
        if (!placeRequest.rankBy.isNullOrEmpty()) { //TODO глянуть, как приходят данные
            url.queryParam("rankBy", placeRequest.rankBy)
        }
        placeRequest.type?.let {
            url.queryParam("rankBy", placeRequest.rankBy)
        }
        return url.queryParam("key", "API_KEY")
            .encode()
            .toUriString().replace('?', '%')
            .replaceFirst('%', '?')//TODO сделать менее бабайским и добавить поля нормально
    }

    //TODO че по стилю
    private fun JsonNode.getPlace() = PlacesResponse(
        name = this["name"].toString(),
        lat = this["geometry"]["location"]["lat"].asDouble(),
        lng = this["geometry"]["location"]["lng"].asDouble(),
        icon = this["icon"].toString(),//мб тоже стоит сделать проверку
        rating = this.get("rating")?.asInt() ?: 0
    )

    @GetMapping("/places")
    @ResponseBody
    fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): Any {//TODO make it suspend
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
                return ResponseEntity.badRequest()//хм
            }
            responseObject["results"].forEach {
                val place = it.getPlace()
                places.add(place)
            }
            return ResponseEntity.ok(places)
        }
        //TODO ошибку тоже обработать
        return ResponseEntity.badRequest()
    }
}