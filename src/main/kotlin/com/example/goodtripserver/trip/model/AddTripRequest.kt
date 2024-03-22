package com.example.goodtripserver.trip.model

import com.goodtrip.goodtripserver.database.models.TripState
import java.sql.Date

data class AddTripRequest(

    val title: String,

    val moneyInUsd: Int,

    val mainPhotoUrl: String?,

    val departureDate: Date,

    val arrivalDate: Date,

    val tripState: TripState,

    val notes: List<AddNoteRequest>,

    val countries: List<AddCountryRequest>
)
