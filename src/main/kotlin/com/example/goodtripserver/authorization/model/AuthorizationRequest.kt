package com.example.goodtripserver.authorization.model

import jakarta.validation.constraints.NotBlank

data class AuthorizationRequest(
    @NotBlank(message = "Login can't be empty")
    val login: String,
    @NotBlank(message = "Password can't be empty")
    val password: String

)
