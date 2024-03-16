package com.example.goodtripserver.trip.model

import com.goodtrip.goodtripserver.database.models.CountryVisit
import com.goodtrip.goodtripserver.database.models.Note
import com.goodtrip.goodtripserver.database.models.TripState
import java.sql.Date


data class AddTripRequest(

    val userId: Int,

    val title: String,

    val moneyInUsd: Int,

    val mainPhotoUrl: String?,

    val departureDate: Date,

    val arrivalDate: Date,

    val tripState: TripState,

    val notes: List<Note>,

    val countries: List<CountryVisit>
)
