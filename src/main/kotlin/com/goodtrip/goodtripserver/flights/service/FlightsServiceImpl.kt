package com.goodtrip.goodtripserver.flights.service

import com.amadeus.Amadeus
import com.amadeus.Params
import com.goodtrip.goodtripserver.flights.model.FlightSegment
import com.goodtrip.goodtripserver.flights.model.FlightsResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class FlightsServiceImpl : FlightsService {

    private val amadeus = Amadeus.builder("FLIGHTS_API_KEY", "FLIGHTS_API_SECRET_KEY").build()

    override fun getFlights(
        origin: String,
        destination: String,
        departDate: String,
        adults: String,
        returnDate: String?
    ): ResponseEntity<List<FlightsResponse>> {
        val result = mutableListOf<FlightsResponse>()
        amadeus.shopping.flightOffersSearch.get(
            Params.with("originLocationCode", origin)
                .and("destinationLocationCode", destination)
                .and("departureDate", departDate)
                .and("adults", adults)
                .also { if (returnDate != null) it.and("returnDate", returnDate) })
            .forEach {
                val segments = it.itineraries.first().segments
                    .map { segment ->
                        FlightSegment(
                            departure = segment.departure.iataCode,
                            departureDate = segment.departure.at,
                            arrival = segment.arrival.iataCode,
                            arrivalDate = segment.arrival.at,
                        )
                    }
                result.add(
                    FlightsResponse(
                        flight = FlightSegment(
                            departure = segments.firstOrNull()!!.departure,
                            departureDate = segments.firstOrNull()!!.departureDate,
                            arrival = segments.lastOrNull()!!.arrival,
                            arrivalDate = segments.lastOrNull()!!.arrival,
                        ),
                        currency = it.price.currency,
                        price = it.price.total,
                        segments = segments
                    )
                )
            }
        if (result.isNotEmpty()) {
            return ResponseEntity(HttpStatus.NO_CONTENT)
        }
        return ResponseEntity.ok(result)
    }

}