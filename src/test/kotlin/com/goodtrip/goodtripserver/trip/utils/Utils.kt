package com.goodtrip.goodtripserver.trip.utils

import com.goodtrip.goodtripserver.database.models.*
import com.goodtrip.goodtripserver.trip.model.AddCountryRequest
import com.goodtrip.goodtripserver.trip.model.AddNoteRequest
import com.goodtrip.goodtripserver.trip.model.AddTripRequest
import com.goodtrip.goodtripserver.trip.model.City
import java.sql.Date

object Utils {
    internal fun tripRequest(): AddTripRequest {
        return AddTripRequest(
            "Russian drill",
            1000,
            null,
            Date.valueOf("2001-09-11"),
            Date.valueOf("2023-01-04"),
            TripState.PLANNED,
            noteRequest(),
            countryRequest()
        )
    }

    internal fun noteRequest(): List<AddNoteRequest> {
        return listOf(
            AddNoteRequest(
                "Dorogoy Dnevnik",
                null,
                "52 (Алло)\n Да здравствует Санкт-Петербург (А), и это город наш (YEEI)",
                "42",
                1
            )
        )
    }

    internal fun countryRequest(): List<AddCountryRequest> {
        return listOf(
            AddCountryRequest(
                "Zimbabve",
                listOf(
                    City(
                        "Moscow",
                        1.0,
                        2.0
                    )
                )
            )
        )
    }

    internal fun getListOfTrips(): List<Trip> {
        return listOf(
            Trip(
                0,
                "Russian drill",
                1000,
                null,
                Date.valueOf("2001-09-11"),
                Date.valueOf("2023-01-04"),
                TripState.PLANNED,
                getListOfNotes(),
                getListOfCountries()
            )
        )
    }

    internal fun getListOfNotes(): List<Note> {
        return listOf(
            Note(
                "Dorogoy Dnevnik",
                null,
                "52 (Алло)\n Да здравствует Санкт-Петербург (А), и это город наш (YEEI)",
                "42",
                1
            ),
            Note(
                "Kruzhka Piva",
                "empty",
                "Normal'no ya tak vipil",
                "10",
                1
            )
        )
    }

    private fun getListOfCountries(): List<CountryVisit> {
        return listOf(
            CountryVisit("Russia", getListOfCities(), 1),
            CountryVisit("Russia2", getListOfCities(), 1)
        )
    }

   private fun getListOfCities(): List<CityVisit> {
        return listOf(
            CityVisit("Magnitogorsk", 0.0, 0.0),
            CityVisit("Tolyatti", 1.0, 1.0),
            CityVisit("Yevpatoria", 2.0, 2.0)
        )
    }
}