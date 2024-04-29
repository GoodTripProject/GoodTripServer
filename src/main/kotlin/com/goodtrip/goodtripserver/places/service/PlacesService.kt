package com.goodtrip.goodtripserver.places.service

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import org.springframework.http.ResponseEntity

interface PlacesService {

    suspend fun getNearPlaces(placeRequest: PlaceRequest): ResponseEntity<Any>

}