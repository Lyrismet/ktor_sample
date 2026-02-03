package ru.example.routing

import kotlinx.serialization.Serializable
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*

@Serializable
data class NameItem(
    val id: Int,
    val name: String
)

val namesStorage = mutableListOf<NameItem>()
var lastId = 0
fun Route.nameRouting() {
    route("/names") {
        get {
            call.respond(namesStorage)
        }

        post {
            val nameText = call.receiveText()
            if (nameText.isNotBlank()) {
                val newItem = NameItem(id = lastId++, name = nameText)
                namesStorage.add(newItem)
                call.respond(HttpStatusCode.Created, newItem)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Имя не может быть пустым")
            }
        }

        put("/{id}") {
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

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            if (namesStorage.removeIf { it.id == id }) {
                call.respondText(
                    "Объект с id $id и именем ${namesStorage.find { it.id == id }?.name} успешно удален",
                    status = HttpStatusCode.OK
                )
            } else {
                call.respond(HttpStatusCode.NotFound, "Нечего удалять")
            }
        }
    }
}