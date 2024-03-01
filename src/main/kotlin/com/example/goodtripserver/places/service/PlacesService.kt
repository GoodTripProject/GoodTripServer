package com.example.goodtripserver.places.service

import com.example.goodtripserver.places.model.PlaceRequest

interface PlacesService {

    fun getNearPlaces(placeRequest: PlaceRequest): Any

}