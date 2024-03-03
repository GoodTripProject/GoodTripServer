package com.example.goodtripserver.places.service

import com.example.goodtripserver.places.model.PlaceRequest
import org.springframework.http.ResponseEntity

interface PlacesService {

    fun getNearPlaces(placeRequest: PlaceRequest): ResponseEntity<Any>

}