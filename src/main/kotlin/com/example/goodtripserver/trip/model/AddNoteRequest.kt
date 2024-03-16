package com.example.goodtripserver.trip.model

import com.goodtrip.goodtripserver.database.models.Note

data class AddNoteRequest(
    val userId: Int,
    val note: Note
)