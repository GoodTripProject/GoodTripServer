package com.goodtrip.goodtripserver.trip.route

import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import com.goodtrip.goodtripserver.trip.service.TripService
import com.goodtrip.goodtripserver.database.models.Trip
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/trip")
class TripController {

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
    fun deleteCountryVisit(@PathVariable countryVisitId: Int): ResponseEntity<String> {
        return tripService.deleteCountryVisit(countryVisitId)
    }
}