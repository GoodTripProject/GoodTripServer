package com.goodtrip.goodtripserver.authentication.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@Builder
data class AuthorizationRequest(
    @NotBlank(message = "Login can't be empty")
    @Email
    val username: String,
    @NotBlank(message = "Password can't be empty")
    val password: String

)
