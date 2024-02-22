package com.example.goodtripserver.places

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

class PlacesResponse(
    @NotBlank(message = "field \"name\" can't be null")
    val name: String,
    @NotNull
    val lat: Double,//мб вообще строка
    @NotNull
    val lng: Double,

    val icon: String,
    @NotNull
    val rating: Int,
)

