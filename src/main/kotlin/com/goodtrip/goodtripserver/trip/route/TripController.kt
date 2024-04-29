package com.goodtrip.goodtripserver.trip.route

import com.goodtrip.goodtripserver.authentication.route.AuthenticationController
import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.database.models.TripView
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import com.goodtrip.goodtripserver.trip.service.TripService
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/trip")
class TripController {
    private val logger = LoggerFactory.getLogger(AuthenticationController::class.java)

    @Autowired
    lateinit var tripService: TripService

    @ResponseBody
    @GetMapping("/all/{userId}")
    fun getUserTrips(@PathVariable userId: Int): ResponseEntity<List<Trip>> {
        return tripService.getTrips(userId)
    }

    @ResponseBody
    @GetMapping("/{tripId}")
    fun getTripById(@PathVariable tripId: Int): ResponseEntity<Any> {
        return tripService.getTrip(tripId)
    }


    @ResponseBody
    @PostMapping("/{userId}")
    fun addTrip(@PathVariable userId: Int, @RequestBody trip: AddTripRequest): ResponseEntity<String> {
        return tripService.addTrip(userId, trip)
    }

    @ResponseBody
    @DeleteMapping("/{tripId}")
    @Transactional
    fun deleteTripById(@PathVariable tripId: Int): ResponseEntity<String> {
        return tripService.deleteTrip(tripId)
    }

    @ResponseBody
    @GetMapping("/note/{noteId}")
    fun getNoteById(@PathVariable noteId: Int): ResponseEntity<Any> {
        return tripService.getNote(noteId)
    }

    @ResponseBody
    @DeleteMapping("/note/{noteId}")
    @Transactional
    fun deleteNoteById(@PathVariable noteId: Int): ResponseEntity<String> {
        return tripService.deleteNote(noteId)
    }

    //TODO чекнуть что с id
    @ResponseBody
    @PostMapping("/note/{userId}")
    fun addNote(@PathVariable userId: Int, @RequestBody note: AddNoteRequest): ResponseEntity<String> {
        return tripService.addNote(userId, note)
    }

    @ResponseBody
    @PostMapping("/country/{tripId}")
    fun addCountryVisit(@PathVariable tripId: Int, @RequestBody country: AddCountryRequest): ResponseEntity<String> {
        return tripService.addCountryVisit(tripId, country)
    }

    @ResponseBody
    @DeleteMapping("/country/{countryVisitId}")
    @Transactional
    fun deleteCountryVisit(@PathVariable countryVisitId: Int): ResponseEntity<String> {
        return tripService.deleteCountryVisit(countryVisitId)
    }

    @ResponseBody
    @PutMapping("/update_trip")
    fun updateTrip(@RequestBody trip: Trip): ResponseEntity<String> {
        return tripService.updateTrip(trip)
    }

    @ResponseBody
    @GetMapping("/get_authors_trips")
    fun getAuthorsTrips(@RequestParam userId: Int, @RequestParam start: Int): ResponseEntity<List<TripView>> {
        val result = tripService.getAuthorsTrips(userId, start)
        logger.debug(result.toString())
        return result
    }
}