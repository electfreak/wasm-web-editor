import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.HTMLUListElement
import org.w3c.fetch.*

private fun getHeaders(): JsAny = js("(new Headers)")

val DEFAULT_LANG = Lang.PLAIN
const val API_DOMAIN = "http://localhost:8082"

fun getRequestInit(method: String, body: JsString?) = RequestInit(
    method = method,
    cache = RequestCache.NO_CACHE,
    credentials = RequestCredentials.OMIT,
    headers = getHeaders(),
    mode = RequestMode.CORS,
    integrity = "",
    redirect = RequestRedirect.FOLLOW,
    referrerPolicy = "strict-origin-when-cross-origin".toJsString(),
    body = body
)


object Global {
    val editorEl = document.querySelector(".editor") as HTMLDivElement
    val controlsEl = document.querySelector(".editor-controls") as HTMLDivElement
    val selectSyntaxEl = controlsEl.querySelector(".select select") as HTMLSelectElement
    val filesEl = document.querySelector(".files ul") as HTMLUListElement
    val fileNameEl = document.querySelector(".filename") as HTMLInputElement
    var currentFile: String? = null

    val editor = monaco.editor.create(editorEl, IEditorConfig(value = "Loading...", language = DEFAULT_LANG))
}
