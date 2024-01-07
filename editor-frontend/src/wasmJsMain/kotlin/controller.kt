object EditorController {
    var currFileName = "untitled"
    private val fileNames = mutableListOf<String>()

    private fun openAnyFile() {
        if (fileNames.isEmpty()) {
            EditorView.fillFile("untitled", "")
        } else {
            openFile(fileNames.first())
        }
    }

    internal fun saveCurrFile() {
        EditorModel.saveFile(currFileName, EditorView.getMonacoEditorValue())
        if (!fileNames.contains(currFileName)) {
            fileNames.add(currFileName)
            EditorView.fillFileNames(fileNames)
        }
    }

    internal fun deleteFile(fileName: String) {
        EditorModel.deleteFile(fileName).then {
            fileNames.remove(fileName)
            EditorView.fillFileNames(fileNames)

            if (fileName == currFileName) {
                openAnyFile()
            }

            null
        }
    }

    internal fun openFile(fileName: String) {
        EditorModel.loadFileContent(fileName).then {
            it as File

            currFileName = fileName
            EditorView.fillFile(fileName, it.body)

            null
        }
    }

    internal fun init() {
        EditorView.init()
        EditorView.fillLangSyntax()
        EditorModel.loadFileNames().then {
            it as JsArray<JsString>

            for (i in 0..<it.length) {
                fileNames.add(it[i].toString())
            }

            EditorView.fillFileNames(fileNames)
            openAnyFile()

            null
        }
    }

}