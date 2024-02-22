package com.example.goodtripserver.places

import PlacesResponce
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
class PlacesControler {


    private fun getUrl(placeRequest: PlaceRequest) =
        UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", placeRequest.location.replaceFirst('%', '?'))//smth here
            .queryParam("radius", placeRequest.radius.toString())
            .queryParam("key", /*TODO*/)
            .encode()
            .toUriString().replace('?', '%').replaceFirst('%', '?')//TODO сделать менее бабайским


    @GetMapping("/places")
    @ResponseBody
            /*suspend*/ fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): Any {//TODO поменять на Array<Place>
        val url = getUrl(placeRequest)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

        val objectMapper = jacksonObjectMapper()//переместить

        if (response.statusCode() == 200) {//TODO сделать с HttpStatus.OK
            val places = mutableListOf<PlacesResponce>()
            val responseObject = objectMapper.readTree(response.body())
            responseObject["results"].forEach {
                val place = PlacesResponce(
                    name = it["name"].toString(),
                    icon = it["icon"].toString(),//мб тоже стоит сделать проверку
                    lat = it["geometry"]["location"]["lat"].asDouble(),
                    lng = it["geometry"]["location"]["lng"].asDouble(),
                    rating = it?.get("rating")?.asInt() ?: 0
                )
                places.add(place)
            }
            return ResponseEntity.ok(places)
        }
        //TODO ошибку тоже обработать
        return ResponseEntity.badRequest()
    }
}