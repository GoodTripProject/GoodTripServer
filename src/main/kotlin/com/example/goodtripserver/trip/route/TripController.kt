package com.example.goodtripserver.trip.route

import com.example.goodtripserver.trip.model.AddCountryRequest
import com.example.goodtripserver.trip.model.AddNoteRequest
import com.example.goodtripserver.trip.model.AddTripRequest
import com.example.goodtripserver.trip.service.TripService
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
    fun getUserTrips(@PathVariable userId: Int): ResponseEntity<List<Trip>> {//TODO понять, как один параметр брать
        return tripService.getTrips(userId)
    }

    @ResponseBody
    @GetMapping("/{tripId}")
    fun getTripById(@PathVariable tripId: Int): ResponseEntity<Any> {
        return tripService.getTrip(tripId)
    }

    //TODO
    @PostMapping
    @ResponseBody
    fun addTrip(@RequestBody trip: AddTripRequest): ResponseEntity<String> {
        return tripService.addTrip(trip)
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

    @DeleteMapping("/note/{noteId}")
    @ResponseBody
    fun deleteNoteById(@PathVariable noteId: Int): ResponseEntity<String> {
        return tripService.deleteNote(noteId)
    }

    //TODO
    @ResponseBody
    @PostMapping("/note/{userId}")
    fun addNote(@RequestBody request: AddNoteRequest): ResponseEntity<String> {
        return tripService.addNote(request)
    }

    //TODO
    @ResponseBody
    @PostMapping("/country/{tripId}")
    fun addCountryVisit(@RequestBody request: AddCountryRequest): ResponseEntity<String> {
        return tripService.addCountryVisit(request)
    }

    //TODO аналогично
    @DeleteMapping("/country/{countryVisitId}")
    @ResponseBody
    fun deleteCountryVisit(@PathVariable countryVisitId: Int): ResponseEntity<String> {
        return tripService.deleteCountryVisit(countryVisitId)
    }
}