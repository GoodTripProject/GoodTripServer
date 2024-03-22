package com.goodtrip.goodtripserver.trip.service

import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import com.goodtrip.goodtripserver.database.models.CityVisit
import com.goodtrip.goodtripserver.database.models.CountryVisit
import com.goodtrip.goodtripserver.database.models.Note
import com.goodtrip.goodtripserver.database.models.Trip
import com.goodtrip.goodtripserver.database.repositories.TripRepositoryImplementation
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class TripServiceImpl : TripService {
//    @Autowired
//    private lateinit var tripRepository: TripRepository //TODO разобраться что не так

    private val tripRepository = TripRepositoryImplementation()

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

    override fun addTrip(userId: Int, trip: AddTripRequest): ResponseEntity<String> {
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
        tripRepository.addTrip(
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

    override fun deleteTrip(tripId: Int): ResponseEntity<String> {
        if (tripRepository.deleteTrip(tripId)) {
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
    override fun addNote(userId: Int, note: AddNoteRequest): ResponseEntity<String> {
        tripRepository.addNote(userId, Note(note.title, note.photoUrl, note.googlePlaceId, note.tripId))
        return ResponseEntity.ok().build()
    }

    override fun deleteNote(noteId: Int): ResponseEntity<String> {
        if (tripRepository.deleteNote(noteId)) {
            return ResponseEntity.ok("Note deleted successfully")
        }
        return ResponseEntity.badRequest().body("Note with id '$noteId' not exist")
    }

    override fun addCountryVisit(tripId: Int, country: AddCountryRequest): ResponseEntity<String> {
        val cities = ArrayList<CityVisit>()
        country.cities.forEach { cities.add(CityVisit(it.city, it.longitude, it.latitude)) }
        tripRepository.addCountryVisit(tripId, CountryVisit(country.country, cities, tripId))
        return ResponseEntity.ok().build()
    }

    override fun deleteCountryVisit(countryVisitId: Int): ResponseEntity<String> {
        if (tripRepository.deleteCountryVisit(countryVisitId)) {
            return ResponseEntity.ok("Country deleted successfully")
        }
        return ResponseEntity.badRequest().body("Country with id '$countryVisitId' not exist")
    }
}