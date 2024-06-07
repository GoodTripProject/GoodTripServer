package com.goodtrip.goodtripserver.flights.route

import com.goodtrip.goodtripserver.flights.service.FlightsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/flights")
class FlightsController {

    @Autowired
    private lateinit var flightsService: FlightsService

    @GetMapping
    suspend fun getFlights(
        @RequestParam(required = true) origin: String,
        @RequestParam(required = true) destination: String,
        @RequestParam(required = true) departureDate: String,
        @RequestParam(required = true) adults: String,
        @RequestParam(required = false) returnDate: String?
    ) = flightsService.getFlights(origin, destination, departureDate, adults, returnDate)
}