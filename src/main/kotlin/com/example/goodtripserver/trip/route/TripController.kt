package com.example.goodtripserver.trip.route

import com.example.goodtripserver.trip.model.AddNoteRequest
import com.example.goodtripserver.trip.model.AddTripRequest
import com.example.goodtripserver.trip.service.TripService
import com.goodtrip.goodtripserver.database.models.Trip
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping("/all")
    @ResponseBody
    fun getUserTrips(@RequestBody userId: Int): ResponseEntity<List<Trip>> {//TODO понять, как один параметр брать
        return tripService.getTrips(userId)
    }

    //TODO сделать, чтобы id был в пути вместо trip
    @GetMapping("/trip")
    @ResponseBody
    fun getTripById(@RequestBody tripId: Int): ResponseEntity<Any> {
        return tripService.getTrip(tripId)
    }

    //TODO аналогично
    @PostMapping("/trip")
    @ResponseBody
    fun addTrip(@RequestBody trip: AddTripRequest): ResponseEntity<String> {
        return tripService.addTrip(trip)
    }

    //TODO аналогично
    @DeleteMapping("/trip")
    @ResponseBody
    fun deleteTripById(@RequestBody tripId: Int): ResponseEntity<String> {
        return tripService.deleteTrip(tripId)
    }

    // TODO аналогично
    @GetMapping("/note")
    @ResponseBody
    fun getNoteById(@RequestBody noteId: Int): ResponseEntity<Any> {
        return tripService.getNote(noteId)
    }

    // TODO аналогично
    @DeleteMapping("/note")
    @ResponseBody
    fun deleteNoteById(@RequestBody noteId: Int): ResponseEntity<String> {
        return tripService.deleteNote(noteId)
    }

    @PostMapping("/note")
    @ResponseBody
    fun addNote(@RequestBody request: AddNoteRequest): ResponseEntity<String> {
        return tripService.addNote(request)
    }

}