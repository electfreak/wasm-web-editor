package com.example

import com.example.plugins.*
import configureDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.cors.routing.*
import org.jetbrains.exposed.sql.Database

fun main() {
    val postgresHostname = System.getenv("POSTGRES_HOSTNAME")
    val postgresPassword = System.getenv("POSTGRES_PASSWORD")
    val postgresUser = System.getenv("POSTGRES_USER")
    val postgresDatabase = System.getenv("POSTGRES_DB")
    val postgresPort = System.getenv("POSTGRES_PORT")

    Database.connect(
        "jdbc:postgresql://$postgresHostname:$postgresPort/$postgresDatabase",
        driver = "org.postgresql.Driver",
        user = postgresUser,
        password = postgresPassword,

    )

    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    configureRouting()
    configureDatabase()
    configureSerialization()

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
    }
}