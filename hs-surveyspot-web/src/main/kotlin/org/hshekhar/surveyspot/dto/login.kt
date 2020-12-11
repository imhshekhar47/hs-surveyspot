package org.hshekhar.surveyspot.dto

data class LoginRequestDTO(
        val username: String,
        val password: String)

data class LoginResponseDTO(
        val username: String,
        val authToken: String,
        val refreshToken: String)