package com.goodtrip.goodtripserver.trip.route

import com.fasterxml.jackson.databind.ObjectMapper
import com.goodtrip.goodtripserver.database.models.TripState
import com.goodtrip.goodtripserver.trip.service.TripService
import com.goodtrip.goodtripserver.trip.utils.Utils
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(MockitoExtension::class)
class TripControllerTest {

    @Mock
    lateinit var tripService: TripService

    @InjectMocks
    lateinit var tripController: TripController

    private lateinit var mockMvc: MockMvc

    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tripController).build()
        objectMapper = ObjectMapper()
    }

    @Test
    fun getUserTrips() {
        `when`(tripService.getTrips(1)).thenReturn(ResponseEntity.ok().body(Utils.getListOfTrips()))
        mockMvc.perform(get("/trip/all/{userId}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(1))//не знаю, как проверить содержимое
        verify(tripService, times(1)).getTrips(1)
    }


    @Test
    fun getTripById() {
        `when`(tripService.getTrip(1)).thenReturn(ResponseEntity.ok().body(Utils.getListOfTrips()[0]))
        mockMvc.perform(get("/trip/{id}", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(0))
            .andExpect(jsonPath("$.title").value("Russian drill"))
            .andExpect(jsonPath("$.moneyInUsd").value(1000))
            .andExpect(jsonPath("$.mainPhotoUrl").value(null))
//            .andExpect(jsonPath( "$.departureDate").value(Date.valueOf("2001-09-11")))
//            .andExpect(jsonPath( "$.arrivalDate").value(Date.valueOf("2023-01-04")))
            .andExpect(jsonPath("$.state").value(TripState.PLANNED.toString()))
            .andExpect(jsonPath("$.notes[0]").value(Utils.getListOfNotes()[0]))
//            .andExpect(jsonPath( "$.visits[0]").value(getListOfCountries()[0]))
        verify(tripService, times(1)).getTrip(1)

    }

    @Test
    fun addTrip() {
        val tripJson = objectMapper.writeValueAsString(Utils.tripRequest())
        `when`(tripService.addTrip(0, Utils.tripRequest())).thenReturn(ResponseEntity.ok().body("All ok"))
        mockMvc.perform(
            post("/trip/{userId}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$").value("All ok"))
        verify(tripService, times(1)).addTrip(0, Utils.tripRequest())

    }

    @Test
    fun deleteTripById() {
        val tripJson = objectMapper.writeValueAsString(Utils.tripRequest())
        `when`(tripService.deleteTrip(0)).thenReturn(ResponseEntity.ok().body("Trip was deleted"))
        mockMvc.perform(
            delete("/trip/{tripId}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tripJson)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Trip was deleted"))
        verify(tripService, times(1)).deleteTrip(0)

    }

    @Test
    fun getNoteById() {
        `when`(tripService.getNote(0)).thenReturn(ResponseEntity.ok().body(Utils.getListOfNotes()[0]))
        mockMvc.perform(get("/trip/note/{noteId}", 0))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Dorogoy Dnevnik"))
            .andExpect(jsonPath("$.photoUrl").value(null))
            .andExpect(jsonPath("$.text").value("52 (Алло)\n Да здравствует Санкт-Петербург (А), и это город наш (YEEI)"))
            .andExpect(jsonPath("$.googlePlaceId").value("42"))
            .andExpect(jsonPath("$.tripId").value(1))
        verify(tripService, times(1)).getNote(0)
    }

    @Test
    fun deleteNoteById() {
        `when`(tripService.deleteNote(0)).thenReturn(ResponseEntity.ok().body("Note was deleted"))
        mockMvc.perform(delete("/trip/note/{noteId}", 0))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Note was deleted"))
        verify(tripService, times(1)).deleteNote(0)
    }

    @Test
    fun addNote() {
        val noteJson = objectMapper.writeValueAsString(Utils.noteRequest()[0])
        `when`(tripService.addNote(0, Utils.noteRequest()[0])).thenReturn(ResponseEntity.ok().body("Note was added"))
        mockMvc.perform(
            post("/trip/note/{userId}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(noteJson)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Note was added"))
        verify(tripService, times(1)).addNote(0, Utils.noteRequest()[0])
    }

    @Test
    fun addCountryVisit() {
        val countryJson = objectMapper.writeValueAsString(Utils.countryRequest()[0])
        `when`(tripService.addCountryVisit(1, Utils.countryRequest()[0])).thenReturn(
            ResponseEntity.ok().body("Country was added")
        )
        mockMvc.perform(
            post("/trip/country/{tripId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(countryJson)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Country was added"))
        verify(tripService, times(1)).addCountryVisit(1, Utils.countryRequest()[0])
    }

    @Test
    fun deleteCountryVisit() {
        `when`(tripService.deleteCountryVisit(0)).thenReturn(ResponseEntity.ok().body("Country was deleted"))
        mockMvc.perform(delete("/trip/country/{countryVisitId}", 0))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").value("Country was deleted"))
        verify(tripService, times(1)).deleteCountryVisit(0)
    }


}