package com.example.goodtripserver.trip.service

import com.example.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.database.models.Trip
import org.springframework.http.ResponseEntity

interface TripService {

    fun getTrips(userId: Int): ResponseEntity<List<Trip>>

    fun getTrip(tripId: Int): ResponseEntity<Any>

    fun deleteTrip(tripId: Int): ResponseEntity<String>

    fun getNote(noteId: Int): ResponseEntity<Any>

    fun addNote(request: AddNoteRequest): ResponseEntity<String>

    fun deleteNote(noteId: Int): ResponseEntity<String>

}