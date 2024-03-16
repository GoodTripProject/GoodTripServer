package com.example.goodtripserver.trip.service

import com.example.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.database.repositories.TripRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TripServiceImpl : TripService {
    @Autowired()
    lateinit var tripRepository: TripRepository //TODO разобраться что не так

    override fun getTrips(userId: Int): ResponseEntity<List<Trip>> {

        val trips = tripRepository.getTrips(userId)
        return ResponseEntity.ok(trips)
    }

    override fun getTrip(tripId: Int): ResponseEntity<Any> {
        val trip = tripRepository.getTripById(tripId)
        if (trip.isEmpty) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok(trip.get())
    }

    override fun deleteTrip(tripId: Int): ResponseEntity<String> {
        if (tripRepository.deleteTrip(tripId)){
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok("Trip deleted successfully")
    }

    override fun getNote(noteId: Int): ResponseEntity<Any> {
        val note = tripRepository.getNoteById(noteId)
        if (note.isEmpty) {
            return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
        }
        return ResponseEntity.ok(note.get())
    }


    //TODO сказать андрею, что лучше возвращать булл, была ли добавлена записка
    override fun addNote(request: AddNoteRequest): ResponseEntity<String> {
        tripRepository.addNote(request.userId, request.note)
        return ResponseEntity.ok().build()
    }

    override fun deleteNote(noteId: Int): ResponseEntity<String> {
        if (tripRepository.deleteNote(noteId)) {
            return ResponseEntity.ok("Note deleted successfully")
        }
        return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
    }
}