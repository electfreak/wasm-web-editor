import org.w3c.dom.HTMLElement

enum class Lang(val lang: String) {
    JS("javascript"),
    HTML("html"),
    JSON("json"),
    CSS("css"),
    TS("typescript"),
    PLAIN("plaintext")
}

fun IEditorConfig(value: String, language: Lang): JsAny {
    return _IEditorConfig(value, language.lang)
}

private fun _IEditorConfig(value: String, language: String): JsAny =
    js("({ value: value, language: language, automaticLayout: true, minimap: { enabled: false } })")

external object Model : JsAny {
    fun setValue(value: JsString)
}

external object Editor : JsAny {
    fun getModel(): Model?
    fun getValue(): String
}

@JsModule("monaco-editor")
external object monaco : JsAny {
    object editor : JsAny {
        fun create(editorEl: HTMLElement, config: JsAny?): Editor
        fun setModelLanguage(model: Model, lang: String)
    }
}
