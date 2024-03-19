package com.example.goodtripserver.trip.model


data class AddNoteRequest(

    val title: String,

    val photoUrl: String?,

    val googlePlaceId: String,

    val tripId: Int?
)