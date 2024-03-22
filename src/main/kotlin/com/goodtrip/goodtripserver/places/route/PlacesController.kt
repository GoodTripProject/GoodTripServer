package com.goodtrip.goodtripserver.places.route

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.service.PlacesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlacesController {

    @Autowired
    private lateinit var placesService: PlacesService

    @ResponseBody
    @PostMapping("/places")
    fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): ResponseEntity<Any> {
        return placesService.getNearPlaces(placeRequest)
    }
}