@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE", "UNCHECKED_CAST")
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
            .then {
                if (!fileNames.contains(currFileName)) {
                    fileNames.add(currFileName)
                    EditorView.fillFileNames(fileNames)
                }

                EditorView.showToast("File $currFileName saved successfully", true)
                null
            }.catch {
                EditorView.showToast(it.toString(), false)
                null
            }
    }

    internal fun deleteFile(fileName: String) {
        EditorModel.deleteFile(fileName)
            .then {
                fileNames.remove(fileName)
                EditorView.fillFileNames(fileNames)
                EditorView.showToast("File $fileName removed successfully", true)

                if (fileName == currFileName) {
                    openAnyFile()
                }
                null
            }.catch {
                EditorView.showToast(it.toString(), false)
                null
            }
    }

    internal fun openFile(fileName: String) {
        EditorModel.loadFileContent(fileName)
            .then {
                it as File

                currFileName = fileName
                EditorView.fillFile(fileName, it.body)
                null
            }.catch {
                EditorView.showToast(it.toString(), false)
                null
            }
    }

    internal fun init() {
        EditorView.init()
        EditorView.fillLangSyntax()
        EditorModel.loadFileNames()
            .then {
                it as JsArray<JsString>

                for (i in 0..<it.length) {
                    fileNames.add(it[i].toString())
                }

                EditorView.fillFileNames(fileNames)
                openAnyFile()
                null
            }.catch {
                EditorView.showToast(it.toString(), false)
                null
            }
    }

}