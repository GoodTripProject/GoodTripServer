package com.goodtrip.goodtripserver.trip.route

import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import com.goodtrip.goodtripserver.trip.service.TripService
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/trip")
class TripController {

    @Autowired
    lateinit var tripService: TripService

    @ResponseBody
    @GetMapping("/all/{userId}")
    suspend fun getUserTrips(@PathVariable userId: Int): ResponseEntity<List<Trip>> {
        return tripService.getTrips(userId)
    }

    @ResponseBody
    @GetMapping("/{tripId}")
    suspend fun getTripById(@PathVariable tripId: Int): ResponseEntity<Any> {
        return tripService.getTrip(tripId)
    }


    @ResponseBody
    @PostMapping("/{userId}")
    suspend fun addTrip(@PathVariable userId: Int, @RequestBody trip: AddTripRequest): ResponseEntity<String> {
        return tripService.addTrip(userId, trip)
    }

    @ResponseBody
    @DeleteMapping("/{tripId}")
    @Transactional
    suspend fun deleteTripById(@PathVariable tripId: Int): ResponseEntity<String> {
        return tripService.deleteTrip(tripId)
    }

    @ResponseBody
    @GetMapping("/note/{noteId}")
    suspend fun getNoteById(@PathVariable noteId: Int): ResponseEntity<Any> {
        return tripService.getNote(noteId)
    }

    @ResponseBody
    @DeleteMapping("/note/{noteId}")
    @Transactional
    suspend fun deleteNoteById(@PathVariable noteId: Int): ResponseEntity<String> {
        return tripService.deleteNote(noteId)
    }

    //TODO чекнуть что с id
    @ResponseBody
    @PostMapping("/note/{userId}")
    suspend fun addNote(@PathVariable userId: Int, @RequestBody note: AddNoteRequest): ResponseEntity<String> {
        return tripService.addNote(userId, note)
    }

    @ResponseBody
    @PostMapping("/country/{tripId}")
    suspend fun addCountryVisit(@PathVariable tripId: Int, @RequestBody country: AddCountryRequest): ResponseEntity<String> {
        return tripService.addCountryVisit(tripId, country)
    }

    @ResponseBody
    @DeleteMapping("/country/{countryVisitId}")
    @Transactional
    suspend fun deleteCountryVisit(@PathVariable countryVisitId: Int): ResponseEntity<String> {
        return tripService.deleteCountryVisit(countryVisitId)
    }
}