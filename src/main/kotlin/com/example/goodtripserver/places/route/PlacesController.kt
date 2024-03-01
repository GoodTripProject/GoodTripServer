package com.example.goodtripserver.places.route

import com.example.goodtripserver.places.model.PlaceRequest
import com.example.goodtripserver.places.service.PlacesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlacesController {

    @Autowired
    private lateinit var placesService: PlacesService
    @GetMapping("/places")
    @ResponseBody
    fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): Any {
        return placesService.getNearPlaces(placeRequest)
    }
}