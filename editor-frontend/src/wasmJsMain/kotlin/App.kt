import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement


fun main() {
    fillLangSyntax()

    Global.selectSyntaxEl.onchange = { _ ->
        monaco.editor.setModelLanguage(Global.editor.getModel()!!, Global.selectSyntaxEl.value)
    }

    Global.fileNameEl.onchange = { _ ->
        Global.currentFile = Global.fileNameEl.value
    }

    loadFiles()

    val saveBtn = document.querySelector(".save button") as HTMLButtonElement
    saveBtn.onclick = {
        saveCurrFile()
    }

}
