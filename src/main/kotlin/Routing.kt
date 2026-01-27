package ru.example

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*

val namesStorage = mutableListOf<String>()
fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/names"){
            call.respond(namesStorage)
        }
        post("/names"){
            val name = call.receiveText()

            if (name.isNotBlank()){
                namesStorage.add(name)
                call.respondText("Имя '$name' добавлено!", status = HttpStatusCode.Created )
            } else {
                call.respondText("Имя не может быть пустым!", status = HttpStatusCode.BadRequest)
            }
        }
        get("/names/{nameID}"){
            val index = call.parameters["nameID"]?.toIntOrNull()
            if (index != null && index >= 0 && index < namesStorage.size){
                val name = namesStorage[index]
                call.respondText("О, это имя '$name'!", status = HttpStatusCode.OK)
            } else {
                call.respondText("Имени с таким индексом нет.", status = HttpStatusCode.NotFound)
            }
        }
        put("/names/{nameID}") {
            val index = call.parameters["nameID"]?.toIntOrNull()
            val newName = call.receiveText()

            if (index != null && index >= 0 && index < namesStorage.size){
                val oldName = namesStorage[index]
                namesStorage[index] = newName
                call.respondText("Имя '$oldName' заменено на '$newName'!", status = HttpStatusCode.OK)
            } else {
                call.respondText("Имени с таким индексом нет.", status = HttpStatusCode.NotFound)
            }
        }
        delete("/names/{nameID}"){
            val index = call.parameters["nameID"]?.toIntOrNull()

            if (index != null && index >= 0 && index < namesStorage.size) {
                val removedName = namesStorage.removeAt(index)
                call.respondText("Имя '$removedName' удалено!", status = HttpStatusCode.OK)
            } else {
                call.respondText("Имени с таким индексом нет.", status = HttpStatusCode.NotFound)
            }
        }
    }
}
