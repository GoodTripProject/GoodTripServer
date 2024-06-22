package com.goodtrip.goodtripserver.flights.model


data class FlightsResponse(
    val flight: FlightSegment,

    val segments: List<FlightSegment>,

    val price: Double,

    val currency: String
)
