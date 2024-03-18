package com.example.goodtripserver.trip.model

import com.goodtrip.goodtripserver.database.models.CityVisit

data class AddCountryRequest(

    val country: String,

    val cities: List<CityVisit>
)
