package com.goodtrip.goodtripserver.trip.service

import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import org.springframework.http.ResponseEntity

interface TripService {

    fun getTrips(userId: Int): ResponseEntity<List<Trip>>

    fun getTrip(tripId: Int): ResponseEntity<Any>

    fun addTrip(userId: Int, trip: AddTripRequest): ResponseEntity<String>

    fun deleteTrip(tripId: Int): ResponseEntity<String>

    fun getNote(noteId: Int): ResponseEntity<Any>

    fun addNote(userId: Int, note: AddNoteRequest): ResponseEntity<String>

    fun deleteNote(noteId: Int): ResponseEntity<String>

    fun addCountryVisit(tripId: Int, country: AddCountryRequest): ResponseEntity<String>

    fun deleteCountryVisit(countryVisitId: Int): ResponseEntity<String>


}