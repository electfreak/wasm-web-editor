import kotlinx.browser.window
import kotlinx.dom.appendElement
import kotlinx.dom.appendText
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLOptionElement
import org.w3c.dom.get

external interface File {
    val name: String
    val body: String
}

private fun removeChildren(element: HTMLElement) {
    while (element.children.length > 0) {
        element.children[0]?.remove()
    }
}

internal fun fillFileNames(fileNames: JsArray<JsString>) {
    removeChildren(Global.filesEl)

    for (i in 0..<fileNames.length) {
        val name = fileNames[i].toString()
        Global.filesEl.appendElement("li") {
            appendElement("button") {
                this as HTMLButtonElement
                type = "button"
                className = "filename-item"
                appendText(name)
                onclick = {
                    loadFile(name)
                }
            }
            appendElement("button") {
                this as HTMLButtonElement
                type = "button"
                className = "filename-close"
                appendText("X")
                onclick = {
                    deleteFile(name)
                }
            }
        }
    }
}

internal fun deleteFile(fileName: String) {
    window.fetch(
        "$API_DOMAIN/file/$fileName",
        getRequestInit(method = "DELETE", body = null)
    )
        .then {
            if (it.ok) {
                if (fileName == Global.currentFile) {
                    Global.currentFile = null
                }
                loadFiles()
                null
            } else {
                window.alert("Error during file deletion")
                null
            }
        }
}

internal fun fillLangSyntax() {
    Lang.entries.forEach { lang ->
        Global.selectSyntaxEl.appendElement("option") {
            this as HTMLOptionElement
            value = lang.lang
            appendText(lang.name)
            if (lang == DEFAULT_LANG)
                selected = true
        }
    }

}

internal fun fillFile(file: File) {
    Global.editor.getModel()!!.setValue(file.body.toJsString())
    Global.fileNameEl.value = file.name
}

internal fun loadFile(fileName: String) {
    window.fetch("$API_DOMAIN/file/$fileName")
        .then { it ->
            if (it.ok) {
                it.json().then {
                    it as File
                    fillFile(it)
                    Global.currentFile = it.name
                    null
                }
            } else {
                window.alert("Error during file loading")
                null
            }
        }
}

internal fun setEmptyFile() {
    Global.currentFile = "untitled"
    Global.fileNameEl.value = "untitled"
    Global.editor.getModel()!!.setValue("".toJsString())
}

internal fun loadFiles() {
    window.fetch("$API_DOMAIN/filenames")
        .then { it ->
            if (it.ok) {
                it.json().then {
                    it as JsArray<JsString>
                    fillFileNames(it)
                    if (Global.currentFile == null) {
                        if (it.length > 0) {
                            loadFile(it[0].toString())
                        } else {
                            setEmptyFile()
                        }
                    }

                    null
                }
            } else {
                window.alert("Error during filenames loading")
                null
            }
        }
}

internal fun saveCurrFile() {
    window.fetch(
        "$API_DOMAIN/file/${Global.currentFile}",
        getRequestInit(method = "POST", body = Global.editor.getValue().toJsString())
    ).then {
        if (it.ok) {
            loadFiles()
            null
        } else {
            window.alert("Error during file saving")
            null
        }
    }
}
