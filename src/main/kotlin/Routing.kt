package ru.example

import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import ru.example.auth.JwtService
import ru.example.routing.authRouting
import ru.example.routing.nameRouting

fun Application.configureRouting(jwtService: JwtService) {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi.json")

        authRouting(jwtService)

        authenticate("auth-jwt") {
            nameRouting()
        }
    }
}