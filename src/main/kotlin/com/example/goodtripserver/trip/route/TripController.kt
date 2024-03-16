package com.example.goodtripserver.trip.route

import com.example.goodtripserver.trip.service.TripService
import com.goodtrip.goodtripserver.database.models.Trip
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping
    @ResponseBody
    fun getTripById(@RequestBody tripId: Int): ResponseEntity<Any> {
        return tripService.getTrip(tripId)
    }


}