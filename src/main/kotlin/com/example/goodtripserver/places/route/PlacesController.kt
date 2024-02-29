package com.example.goodtripserver.places.route

import com.example.goodtripserver.places.model.PlaceRequest
import com.example.goodtripserver.places.service.PlacesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PlacesController {

    @GetMapping("/places")
    @ResponseBody
    fun getNearPlaces(@RequestBody placeRequest: PlaceRequest): Any {//TODO make it suspend
        return PlacesService().getNearPlaces(placeRequest)
    }
}