package com.goodtrip.goodtripserver.places.route

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.service.PlacesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlacesController {

    @Autowired
    private lateinit var placesService: PlacesService

    @ResponseBody
    @PostMapping("/places")
    suspend fun getNearPlaces(@RequestBody placeRequest: PlaceRequest) = placesService.getNearPlaces(placeRequest)

    @ResponseBody
    @GetMapping("/coordinates")
    fun getCoordinates(@RequestParam city: String) = placesService.getCoordinates(city)

}