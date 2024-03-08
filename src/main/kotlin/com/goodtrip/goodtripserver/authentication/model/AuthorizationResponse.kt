package com.goodtrip.goodtripserver.authentication.model

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@Builder
@NoArgsConstructor
@AllArgsConstructor
data class AuthorizationResponse(
    val handle: String,
    val name: String,
    val surname: String,
    val token: String,
    val url: java.net.URL
)
