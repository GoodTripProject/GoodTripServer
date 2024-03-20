package com.goodtrip.goodtripserver.places.service

import com.goodtrip.goodtripserver.places.model.PlaceRequest
import com.goodtrip.goodtripserver.places.model.PlacesResponse
import org.springframework.http.ResponseEntity

interface PlacesService {

    fun getNearPlaces(placeRequest: PlaceRequest): List<PlacesResponse>?

}