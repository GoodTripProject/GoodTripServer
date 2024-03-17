package com.example.goodtripserver.trip.model

import com.goodtrip.goodtripserver.database.models.CountryVisit

data class AddCountryRequest(

    val tripId: Int,

    val countryVisit: CountryVisit
)
