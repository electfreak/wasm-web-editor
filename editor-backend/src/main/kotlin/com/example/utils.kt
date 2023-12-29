import com.example.models.Files
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    transaction {
        SchemaUtils.create(Files)
    }
}