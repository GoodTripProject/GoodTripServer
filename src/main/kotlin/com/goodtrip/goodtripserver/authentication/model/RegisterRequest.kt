package com.goodtrip.goodtripserver.authentication.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import lombok.Builder

@Builder
data class RegisterRequest(
    @NotBlank
    @Email
    val username: String,
    @NotBlank
    val handle: String,
    @NotBlank
    val password: String,
    @NotBlank
    val name: String,
    @NotBlank
    val surname: String
)
