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

    //    @Value("FLIGHTS_API_KEY")
//    private lateinit var apiKey: String
    //TODO разобраться

    //    @Value("FLIGHTS_API_SECRET_KEY")
//    private lateinit var secretKey: String

//    init {
//        val prop = Properties()
//        val stream = FileInputStream("application.properties")
//        prop.load(stream);
//        secretKey = prop.getProperty("FLIGHTS_API_SECRET_KEY")
//        apiKey = prop.getProperty("FLIGHTS_API_KEY")
//        println("apiKey: $apiKey")
//    }


    private var amadeus = Amadeus.builder("API_KEY", "SECRET_API_KEY").build()

    //TODO потом удалить
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
                            departure = segments.first.departure,
                            departureDate = segments.first.departureDate,
                            arrival = segments.last.arrival,
                            arrivalDate = segments.last.arrival,
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