package com.example.goodtripserver.places

import jakarta.validation.constraints.NotBlank

data class PlacesResponse(
    @NotBlank(message = "field \"name\" can't be null")

    val name: String,

    val lat: Double,//мб вообще строка

    val lng: Double,

    val icon: String,//TODO поменять на фото

    val rating: Int,
)

