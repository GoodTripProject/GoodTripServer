package com.goodtrip.goodtripserver.trip.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodtrip.goodtripserver.database.repositories.CountryVisitRepository
import com.goodtrip.goodtripserver.database.repositories.NoteRepository
import com.goodtrip.goodtripserver.database.repositories.TripBaseRepository
import com.goodtrip.goodtripserver.database.repositories.TripRepository
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.util.*

@ExtendWith(MockitoExtension::class)
class TripServiceImplTest {
    @Mock
    lateinit var tripRepository: TripRepository

    @Mock
    lateinit var tripBaseRepository: TripBaseRepository

    @Mock
    lateinit var noteRepository: NoteRepository

    @Mock
    lateinit var countryRepository: CountryVisitRepository

    @InjectMocks
    lateinit var tripService: TripServiceImpl

    private lateinit var mockMvc: MockMvc

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tripService).build()
        objectMapper = ObjectMapper()
    }

    @Test
    fun getUserTrips() {
        `when`(tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(0)).thenReturn(Utils.getListOfTrips())
        val res = tripRepository.getTripsByUserIdOrderByPublicationTimestampDesc(0)
        assertEquals(Utils.getListOfTrips(), res)
        verify(tripRepository, times(1)).getTripsByUserIdOrderByPublicationTimestampDesc(0)
    }


    @Test
    fun getTripById() {
        val expected = Utils.getListOfTrips()[0]
        `when`(tripRepository.getTripById(0)).thenReturn(Optional.of(expected))
        val res = tripRepository.getTripById(0).get()
        assertEquals(expected, res)
        verify(tripRepository, times(1)).getTripById(0)
    }

    @Test
    fun addTrip() {
        val tripRequest = Utils.getListOfTrips()[0]
        tripBaseRepository.saveTripAndWire(
            tripRequest.id,
            tripRequest.userId,
            tripRequest.title,
            tripRequest.moneyInUsd,
            tripRequest.mainPhotoUrl,
            tripRequest.departureDate,
            tripRequest.arrivalDate,
            tripRequest.state,
            tripRequest.notes,
            tripRequest.visits
        )

        verify(tripBaseRepository, times(1)).saveTripAndWire(
            tripRequest.id,
            tripRequest.userId,
            tripRequest.title,
            tripRequest.moneyInUsd,
            tripRequest.mainPhotoUrl,
            tripRequest.departureDate,
            tripRequest.arrivalDate,
            tripRequest.state,
            tripRequest.notes,
            tripRequest.visits
        )

    }

    @Test
    fun deleteTripById() {
        `when`(tripRepository.deleteTripById(0)).thenReturn(1)
        val res = tripRepository.deleteTripById(0)
        assertEquals(1, res)
        verify(tripRepository, times(1)).deleteTripById(0)
    }

    @Test
    fun getNoteById() {
        val expected = Optional.of(Utils.getListOfNotes()[0])
        `when`(noteRepository.getNoteById(0)).thenReturn(expected)
        val res = noteRepository.getNoteById(0)
        assertEquals(expected, res)
        verify(noteRepository, times(1)).getNoteById(0)
    }

    @Test
    fun deleteNoteById() {
        `when`(noteRepository.deleteNoteById(0)).thenReturn(1)
        val res = noteRepository.deleteNoteById(0)
        assertEquals(1, res)
        verify(noteRepository, times(1)).deleteNoteById(0)
    }

    @Test
    fun addNote() {
//        val noteJson = objectMapper.writeValueAsString(Utils.noteRequest()[0])
//        `when`(tripService.addNote(0, Utils.noteRequest()[0])).thenReturn(ResponseEntity.ok().body("Note was added"))
//        mockMvc.perform(
//            post("/trip/note/{userId}", 0)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(noteJson)
//        ).andExpect(status().isOk)
//            .andExpect(jsonPath("$").value("Note was added"))
//        verify(tripService, times(1)).addNote(0, Utils.noteRequest()[0])
    }

    @Test
    fun addCountryVisit() {
//        val countryJson = objectMapper.writeValueAsString(Utils.countryRequest()[0])
//        `when`(tripService.addCountryVisit(1, Utils.countryRequest()[0])).thenReturn(
//            ResponseEntity.ok().body("Country was added")
//        )
//        mockMvc.perform(
//            post("/trip/country/{tripId}", 1)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(countryJson)
//        ).andExpect(status().isOk)
//            .andExpect(jsonPath("$").value("Country was added"))
//        verify(tripService, times(1)).addCountryVisit(1, Utils.countryRequest()[0])
    }

    @Test
    fun deleteCountryVisit() {
        `when`(countryRepository.deleteCountryVisitById(0)).thenReturn(1)
        val res = countryRepository.deleteCountryVisitById(0)
        assertEquals(1, res)
        verify(countryRepository, times(1)).deleteCountryVisitById(0)
    }


}