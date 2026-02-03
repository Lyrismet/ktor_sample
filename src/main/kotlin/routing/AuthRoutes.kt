package ru.example.routing

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import ru.example.auth.JwtService
import ru.example.auth.LoginRequest

fun Route.authRouting(jwtService: JwtService) {
    post("/login") {
        val request = call.receive<LoginRequest>()
        if (request.userId == "admin" && request.password == "123") {
            val tokens = jwtService.createTokenPair(request.userId)
            call.respond(tokens)
        } else {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}