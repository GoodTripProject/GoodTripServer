package com.goodtrip.goodtripserver.flights.service

import com.goodtrip.goodtripserver.flights.model.FlightsResponse
import org.springframework.http.ResponseEntity

interface FlightsService {
    fun getFlights(
        origin: String,
        destination: String,
        departDate: String,
        adults: String,
        returnDate: String?
    ): ResponseEntity<List<FlightsResponse>>
}