import kotlinx.dom.appendElement
import kotlinx.dom.appendText
import org.w3c.dom.*

external interface File {
    val name: String
    val body: String
}

object EditorView {
    private fun removeChildren(element: HTMLElement) {
        while (element.children.length > 0) {
            element.children[0]?.remove()
        }
    }

    internal fun fillFileNames(fileNames: List<String>) {
        removeChildren(JSObjects.filesEl)
        for (name in fileNames) {
            JSObjects.filesEl.appendElement("li") {
                appendElement("button") {
                    this as HTMLButtonElement
                    type = "button"
                    className = "filename-item"
                    appendText(name)
                    onclick = {
                        EditorController.openFile(name)
                    }
                }
                appendElement("button") {
                    this as HTMLButtonElement
                    type = "button"
                    className = "filename-close"
                    appendElement("img") {
                        this as HTMLImageElement
                        src = "/cross.svg"
                        width = 10
                    }

                    onclick = {
                        EditorController.deleteFile(name)
                    }
                }
            }
        }
    }

    internal fun fillLangSyntax() {
        Lang.entries.forEach { lang ->
            JSObjects.selectSyntaxEl.appendElement("option") {
                this as HTMLOptionElement
                value = lang.lang
                appendText(lang.name)
                if (lang == DEFAULT_LANG)
                    selected = true
            }
        }

    }

    internal fun fillFile(name: String, content: String) {
        JSObjects.fileNameEl.value = name
        JSObjects.monacoEditor.getModel()!!.setValue(content.toJsString())
    }

    internal fun getMonacoEditorValue(): String {
        return JSObjects.monacoEditor.getValue()
    }

    internal fun init() {
        JSObjects.selectSyntaxEl.onchange = { _ ->
            monaco.editor.setModelLanguage(JSObjects.monacoEditor.getModel()!!, JSObjects.selectSyntaxEl.value)
        }

        JSObjects.fileNameEl.onchange = { _ ->
            EditorController.currFileName = JSObjects.fileNameEl.value
        }

        JSObjects.saveEl.onclick = {
            EditorController.saveCurrFile()
        }
    }
}
