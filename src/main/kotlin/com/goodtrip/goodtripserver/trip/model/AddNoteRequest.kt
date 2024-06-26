package com.goodtrip.goodtripserver.trip.model


data class AddNoteRequest(

    val title: String,

    val photoUrl: String?,

    val text: String,

    val googlePlaceId: String,

    val tripId: Int
)