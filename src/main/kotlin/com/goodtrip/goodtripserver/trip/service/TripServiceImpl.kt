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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
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

    override suspend fun getTrips(userId: Int): ResponseEntity<List<Trip>> {
        val trips = withContext(Dispatchers.IO) {
            tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(userId)}
        return ResponseEntity.ok(trips)
    }

    override suspend fun getTrip(tripId: Int): ResponseEntity<Any> {
        val trip = withContext(Dispatchers.IO) {
            tripRepository.getTripById(tripId)
        }
        if (trip.isEmpty) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok(trip.get())
    }

    override suspend fun addTrip(userId: Int, trip: AddTripRequest): ResponseEntity<String> {
        if (!withContext(Dispatchers.IO) {
                authenticationRepository.existsById(userId)
            }) {
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
        withContext(Dispatchers.IO) {
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
        }
        return ResponseEntity.ok().build()
    }

    @Transactional
    override suspend fun deleteTrip(tripId: Int): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                tripRepository.deleteTripById(tripId)
            } <= 0) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        return ResponseEntity.ok("Trip deleted successfully")
    }

    override suspend fun getNote(noteId: Int): ResponseEntity<Any> {
        val note = withContext(Dispatchers.IO) {
            noteRepository.getNoteById(noteId)
        }
        if (note.isEmpty) {
            return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
        }
        return ResponseEntity.ok(note.get())
    }


    override suspend fun addNote(userId: Int, note: AddNoteRequest): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                tripRepository.existsById(note.tripId)
            }) {
            withContext(Dispatchers.IO) {
                noteRepository.save(Note(note.title, note.photoUrl, note.text, note.googlePlaceId, note.tripId))
            }
            return ResponseEntity.ok().build()
        }
        return ResponseEntity.badRequest().body("Trip with id '${note.tripId}' not exist")
    }

    @Transactional
    override suspend fun deleteNote(noteId: Int): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                noteRepository.deleteNoteById(noteId)
            } > 0) {
            return ResponseEntity.ok("Note deleted successfully")
        }
        return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
    }

    override suspend fun addCountryVisit(tripId: Int, country: AddCountryRequest): ResponseEntity<String> {
        if (!withContext(Dispatchers.IO) {
                tripRepository.existsById(tripId)
            }) {
            return ResponseEntity.badRequest().body("Trip with id '$tripId' not exist")
        }
        val cities = ArrayList<CityVisit>()
        country.cities.forEach { cities.add(CityVisit(it.city, it.longitude, it.latitude)) }
        withContext(Dispatchers.IO) {
            countryVisitRepository.save(CountryVisit(country.country, cities, tripId))
        }
        return ResponseEntity.ok().build()
    }

    @Transactional
    override suspend fun deleteCountryVisit(countryVisitId: Int): ResponseEntity<String> {
        if (withContext(Dispatchers.IO) {
                countryVisitRepository.deleteCountryVisitById(countryVisitId)
            } > 0) {
            return ResponseEntity.ok("Country deleted successfully")
        }
        return ResponseEntity.badRequest().body("Country with id '$countryVisitId' not exist")
    }

    override suspend fun updateTrip(trip: Trip): ResponseEntity<String> {
        if (!withContext(Dispatchers.IO) {
                tripRepository.existsById(trip.id)
            }) {
            return ResponseEntity.badRequest().body("Trip with id '${trip.id}' not exist")
        }
        withContext(Dispatchers.IO) {
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
        }
        return ResponseEntity.ok("Trip updated successfully")
    }

    override suspend fun getAuthorsTrips(userId: Int, start: Int): ResponseEntity<List<TripView>> {
        val trips =
            withContext(Dispatchers.IO) {
                tripRepository.getAuthorsTrips(userId, start)
            }
        return ResponseEntity.ok().body(trips)
    }

    override suspend fun getAuthorTrips(handle: String): ResponseEntity<List<Trip>> {
        val userId: Int
        try {
            userId = withContext(Dispatchers.IO) {
                authenticationRepository.getUserByHandle(handle)
            }.get().id
        } catch (e: NoSuchElementException) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        return ResponseEntity.ok(withContext(Dispatchers.IO) {
            tripRepository.getTripsOfSpecificUser(userId)
        })
    }

}
