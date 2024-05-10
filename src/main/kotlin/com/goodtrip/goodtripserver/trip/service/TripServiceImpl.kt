package com.goodtrip.goodtripserver.trip.service

import com.goodtrip.goodtripserver.database.models.*
import com.goodtrip.goodtripserver.database.repositories.AuthenticationRepository
import com.goodtrip.goodtripserver.database.repositories.CountryVisitRepository
import com.goodtrip.goodtripserver.database.repositories.NoteRepository
import com.goodtrip.goodtripserver.database.repositories.TripRepository
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TripServiceImpl : TripService {
    @Autowired
    private lateinit var tripRepository: TripRepository

    @Autowired
    private lateinit var noteRepository: NoteRepository

    @Autowired
    private lateinit var countryVisitRepository: CountryVisitRepository

    @Autowired
    private lateinit var authenticationRepository: AuthenticationRepository
    override fun getTrips(userId: Int): ResponseEntity<List<Trip>> {

        val trips = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(userId)
        return ResponseEntity.ok(trips)
    }

    override fun getTrip(tripId: Int): ResponseEntity<Any> {
        val trip = tripRepository.getTripById(tripId)
        if (trip.isEmpty) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok(trip.get())
    }

    override fun addTrip(userId: Int, trip: AddTripRequest): ResponseEntity<String> {
        if (!authenticationRepository.existsById(userId)) {
            return ResponseEntity.badRequest().body("User with id '${userId}' not exist")
        }
        val countries = ArrayList<CountryVisit>()
        trip.countries.forEach { it ->
            val country = ArrayList<CityVisit>()
            it.cities.forEach {
                country.add(CityVisit(it.city, it.longitude, it.latitude))
            }
            countries.add(CountryVisit(it.country, country))

        }
        val notes = trip.notes.stream()
            .map { Note(it.title, it.photoUrl, it.googlePlaceId) }
            .toList()
        tripRepository.saveTripAndWire(
            null,
            userId,
            trip.title,
            trip.moneyInUsd,
            trip.mainPhotoUrl,
            trip.departureDate,
            trip.arrivalDate,
            trip.tripState,
            notes,
            countries
        )
        return ResponseEntity.ok().build()
    }

    @Transactional
    override fun deleteTrip(tripId: Int): ResponseEntity<String> {
        if (tripRepository.deleteTripById(tripId) <= 0) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok("Trip deleted successfully")
    }

    override fun getNote(noteId: Int): ResponseEntity<Any> {
        val note = noteRepository.getNoteById(noteId)
        if (note.isEmpty) {
            return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
        }
        return ResponseEntity.ok(note.get())
    }


    override fun addNote(userId: Int, note: AddNoteRequest): ResponseEntity<String> {
        if (tripRepository.existsById(note.tripId)) {
            noteRepository.save(Note(note.title, note.photoUrl, note.text, note.googlePlaceId, note.tripId))
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().body("Trip with id '${note.tripId}' not exist")
    }

    @Transactional
    override fun deleteNote(noteId: Int): ResponseEntity<String> {
        if (noteRepository.deleteNoteById(noteId) > 0) {
            return ResponseEntity.ok("Note deleted successfully")
        }
        return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
    }

    override fun addCountryVisit(tripId: Int, country: AddCountryRequest): ResponseEntity<String> {
        if (!tripRepository.existsById(tripId)) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        val cities = ArrayList<CityVisit>()
        country.cities.forEach { cities.add(CityVisit(it.city, it.longitude, it.latitude)) }
        countryVisitRepository.save(CountryVisit(country.country, cities, tripId))
        return ResponseEntity.ok().build()
    }

    @Transactional
    override fun deleteCountryVisit(countryVisitId: Int): ResponseEntity<String> {
        if (countryVisitRepository.deleteCountryVisitById(countryVisitId) > 0) {
            return ResponseEntity.ok("Country deleted successfully")
        }
        return ResponseEntity.badRequest().body("Country with id '$countryVisitId' not exist")
    }

    override fun updateTrip(trip: Trip): ResponseEntity<String> {
        if (!tripRepository.existsById(trip.id)) {
            return ResponseEntity.badRequest().body("Trip with id '${trip.id}' not exist")
        }
        tripRepository.saveTripAndWire(
            trip.id,
            trip.userId,
            trip.title,
            trip.moneyInUsd,
            trip.mainPhotoUrl,
            trip.departureDate,
            trip.arrivalDate,
            trip.state,
            trip.notes,
            trip.visits
        )
        return ResponseEntity.ok("Trip updated successfully")
    }

    override fun getAuthorsTrips(userId: Int, start: Int): ResponseEntity<List<TripView>> {
        return ResponseEntity.ok().body(tripRepository.getAuthorsTrips(userId, start))
    }


}