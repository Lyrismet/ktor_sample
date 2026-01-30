package ru.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class NameItem(
    val id: Int,
    val name: String
)

val namesStorage = mutableListOf<NameItem>()
var lastId = 0;
fun Application.configureRouting() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi.json")
        get("/") {
            call.respondText("Добавьте '/swagger' чтобы увидеть документацию")
        }

        get("/names") {
            call.respond(namesStorage)
        }

        post("/names") {
            val nameText = call.receiveText()
            if (nameText.isNotBlank()) {
                val newItem = NameItem(id = lastId++, name = nameText)
                namesStorage.add(newItem)
                call.respond(HttpStatusCode.Created, newItem)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Имя не может быть пустым")
            }
        }

        put("/names/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val newName = call.receiveText()
            val item = namesStorage.find { it.id == id }

            if (item != null && newName.isNotBlank()) {
                val updatedItem = item.copy(name = newName)
                val index = namesStorage.indexOf(item)
                namesStorage[index] = updatedItem

                call.respond(HttpStatusCode.OK, updatedItem)
            } else {
                call.respond(HttpStatusCode.NotFound, "Объект с id $id не найден")
            }
        }

        delete("/names/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (namesStorage.removeIf { it.id == id }) {
                call.respondText("Объект с id $id удален", status = HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound, "Нечего удалять")
            }
        }
    }
}