package com.example.goodtripserver.trip.model

data class AddCountryRequest(

    val country: String,

    val cities: List<City>
)
