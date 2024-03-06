package com.goodtrip.goodtripserver.authorization.model

import jakarta.validation.constraints.NotBlank

data class RegistrationRequest(
    @NotBlank
    val username: String,
    @NotBlank
    val handle: String,
    @NotBlank
    val password: String,
//    val token TODO понять
    @NotBlank
    val name: String,
    @NotBlank
    val surname: String
)
