package com.example.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class File(val name: String, val body: String)

fun getFileByName(name: String): File? {
    val result = transaction {
        Files.select {
            Files.name eq name
        }.singleOrNull()
    } ?: return null

    return File(result[Files.name], result[Files.body])
}

fun deleteFileByName(fileName: String): Boolean {
    return transaction {
        Files.deleteWhere { name eq fileName } > 0
    }
}

object Files : Table() {
    val name = varchar("title", 128)
    val body = text("body")

    override val primaryKey = PrimaryKey(name)
}