package com.example.goodtripserver.places

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

class PlaceParams(
    @NotBlank(message = "field \"location\" can't be null")
    val location: String,

    @NotNull
    val radius: Int
)