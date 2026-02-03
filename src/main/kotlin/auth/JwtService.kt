package ru.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtService {
    private val secret = "secret-key"
    private val issuer = "ru.example"
    private val algorithm = Algorithm.HMAC256(secret)
    fun createTokenPair(userId: String): TokenPair {
        val accessToken = JWT.create()
            .withSubject(userId)
            .withIssuer(issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + 900_000)) // 15 минут
            .sign(algorithm)
        val refreshToken = JWT.create()
            .withSubject(userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 86_400_000)) // 1 день
            .sign(algorithm)

        return TokenPair(accessToken, refreshToken)
    }

    fun getVerifier(): JWTVerifier = JWT.require(algorithm).withIssuer(issuer).build()
}
