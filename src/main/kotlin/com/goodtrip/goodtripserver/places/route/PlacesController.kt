package com.goodtrip.goodtripserver.places.route

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.model.PlacesResponse
import com.goodtrip.goodtripserver.places.service.PlacesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class PlacesController {

    @Autowired
    private lateinit var placesService: PlacesService
    @GetMapping("/places")
    @ResponseBody
    fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): List<PlacesResponse>? {
        println("getNearPlaces + $placeRequest")
        return placesService.getNearPlaces(placeRequest)
    }

    @GetMapping("/hello")
    @ResponseBody
    fun getNearPlaces(): String {
        return "Hello bitch"
    }
}