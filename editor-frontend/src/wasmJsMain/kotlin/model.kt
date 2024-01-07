import kotlinx.browser.window
import kotlin.js.Promise

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UNCHECKED_CAST")
object EditorModel {
    internal fun saveFile(fileName: String, content: String) {
        window.fetch(
            "$API_DOMAIN/file/$fileName",
            getRequestInit(method = "POST", body = content.toJsString())
        ).then {
            if (it.ok) {
                null
            } else {
                window.alert("Error during file saving")
                null
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
                    window.alert("Error during file deletion")
                    null
                }
            }
    }

    internal fun loadFileContent(fileName: String): Promise<JsAny?> {
        return window.fetch("$API_DOMAIN/file/$fileName")
            .then { it ->
                if (it.ok) {
                    it.json().then {
                        it as File
                        it
                    }
                } else {
                    window.alert("Error during file loading")
                    null
                }
            }
    }

    internal fun loadFileNames(): Promise<JsAny?> {
        return window.fetch("$API_DOMAIN/filenames")
            .then { it ->
                if (it.ok) {
                    it.json().then {
                        it as JsArray<JsString>
                        it
                    }
                } else {
                    window.alert("Error during filenames loading")
                    null
                }
            }
    }
}
