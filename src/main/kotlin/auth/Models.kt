package ru.example.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val userId: String, val password: String)

@Serializable
data class TokenPair(val accessToken: String, val refreshToken: String)