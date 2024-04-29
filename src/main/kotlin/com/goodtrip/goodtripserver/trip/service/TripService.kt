package com.goodtrip.goodtripserver.trip.service

import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import org.springframework.http.ResponseEntity

interface TripService {

    suspend fun getTrips(userId: Int): ResponseEntity<List<Trip>>

    suspend fun getTrip(tripId: Int): ResponseEntity<Any>

    suspend fun addTrip(userId: Int, trip: AddTripRequest): ResponseEntity<String>

    suspend fun deleteTrip(tripId: Int): ResponseEntity<String>

    suspend fun getNote(noteId: Int): ResponseEntity<Any>

    suspend fun addNote(userId: Int, note: AddNoteRequest): ResponseEntity<String>

    suspend fun deleteNote(noteId: Int): ResponseEntity<String>

    suspend fun addCountryVisit(tripId: Int, country: AddCountryRequest): ResponseEntity<String>

    suspend fun deleteCountryVisit(countryVisitId: Int): ResponseEntity<String>


}