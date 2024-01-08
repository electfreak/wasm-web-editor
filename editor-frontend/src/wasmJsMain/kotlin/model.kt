import kotlinx.browser.window
import kotlin.js.Promise

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UNCHECKED_CAST")
object EditorModel {
    internal fun saveFile(fileName: String, content: String): Promise<JsAny?> {
        return window.fetch(
            "$API_DOMAIN/file/$fileName",
            getRequestInit(method = "POST", body = content.toJsString())
        ).then {
            if (it.ok) {
                null
            } else {
                throw Error("Error during file saving")
            }
        }
    }

    internal fun deleteFile(fileName: String): Promise<JsAny?> {
        return window.fetch(
            "$API_DOMAIN/file/$fileName",
            getRequestInit(method = "DELETE", body = null)
        )
            .then {
                if (it.ok) {
                    null
                } else {
                    throw Error("Error during file deletion")
                }
            }
    }

    internal fun loadFileContent(fileName: String): Promise<JsAny?> {
        return window.fetch("$API_DOMAIN/file/$fileName")
            .then { it ->
                if (it.ok) {
                    it.json().then {
                        it
                    }
                } else {
                    throw Error("Error during file loading")
                }
            }
    }

    internal fun loadFileNames(): Promise<JsAny?> {
        return window.fetch("$API_DOMAIN/filenames")
            .then { it ->
                if (it.ok) {
                    it.json().then {
                        it
                    }
                } else {
                    throw Error("Error during filenames loading")
                }
            }
    }
}
