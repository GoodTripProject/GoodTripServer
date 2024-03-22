package com.goodtrip.goodtripserver.places.model

import jakarta.validation.constraints.NotBlank

data class PlaceRequest(
    @NotBlank(message = "field \"location\" can't be null")
    val location: String,

    val radius: Int,

    val rankBy: String?,

    val type: PlacesTypes?
)

enum class PlacesTypes {
    amusement_park,
    aquarium,
    art_gallery,
    cafe,
    casino,
    church,
    city_hall,
    hindu_temple,
    library,
    local_government_office,
    mosque,
    movie_theater,
    museum,
    night_club,
    park,
    restaurant,
    stadium,
    subway_station,
    synagogue,
    tourist_attraction,
    zoo
}