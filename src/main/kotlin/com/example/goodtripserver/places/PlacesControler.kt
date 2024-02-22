package com.example.goodtripserver.places

//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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


    private fun getUrl(placeParams: PlaceParams) =
        UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
            .queryParam("location", placeParams.location.replaceFirst('%', '?'))//smth here
            .queryParam("radius", placeParams.radius.toString())
            .queryParam("key", "API_TOKEN")
            .encode()
            .toUriString().replace('?', '%').replaceFirst('%', '?')//TODO сделать менее бабайским


    @GetMapping("/places")
    @ResponseBody
            /*suspend*/ fun getNearPlaces(@RequestBody placeParams: PlaceParams): ResponseEntity<String> {//TODO поменять на Array<Place>
        val url = getUrl(placeParams)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())

//        val objectMapper = jacksonObjectMapper()//понадобится

        //TODO ошибку тоже обработать
        return ResponseEntity.ok(response.body())
    }
}