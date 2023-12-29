package com.example.plugins

import com.example.models.Files
import com.example.models.deleteFileByName
import com.example.models.getFileByName
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        get("/filenames") {
            val filenames = transaction {
                Files.selectAll().map {
                    it[Files.name]
                }
            }

            call.respond(filenames)
        }

        get("/file/{filename}") {
            val filename = call.parameters.getOrFail("filename")
            val file = getFileByName(filename) ?: call.respond(HttpStatusCode.NotFound)
            call.respond(file)
        }

        post("/file/{filename}") {
            val filename = call.parameters.getOrFail("filename")
            val text = call.receiveText()

            transaction {
                Files.upsert {
                    it[name] = filename
                    it[body] = text
                }
            }

            call.respond(HttpStatusCode.OK)
        }

        delete("/file/{filename}") {
            val filename = call.parameters.getOrFail("filename")
            call.respond(if (deleteFileByName(filename)) HttpStatusCode.OK else HttpStatusCode.NotFound)
        }

    }
}
