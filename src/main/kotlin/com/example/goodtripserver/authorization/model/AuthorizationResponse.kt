package com.example.goodtripserver.authorization.model


data class AuthorizationResponse(
    val handle: String,
    val name: String,
    val surname: String,
    val token: String,
    val url: java.net.URL
)
