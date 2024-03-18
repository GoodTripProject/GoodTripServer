package com.example.goodtripserver.trip.model


data class AddNoteRequest(

    val userId: Int,

    val title: String,

    val photoUrl: String?,

    val googlePlaceId: String,

    val tripId: Int
)