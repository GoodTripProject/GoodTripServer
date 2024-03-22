package com.goodtrip.goodtripserver.trip.model

data class AddCountryRequest(

    val country: String,

    val cities: List<City>
)
