package com.mitu.bloctemplate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages

class GeneratorAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialogResult = Messages.showInputDialogWithCheckBox(
            "Enter bloc name", "Generate Bloc", "Use folder",
            true, true, null, null, FileNameInputValidator()
        )

        generatorFileName(dialogResult.first, dialogResult.second, e)
    }

    private fun capitalizeWords(input: String): String {
        return input.replace(Regex("\\b[a-z]")) { match ->
            match.value.uppercase()
        }
    }

    private fun generatorFileName(fileName: String, userFolder: Boolean, event: AnActionEvent) {
        val blocNameLowerCase = fileName.lowercase()
        val blocClassName = capitalizeWords(fileName)
        val newFileName = fileName.lowercase()

        val screenInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/screen_template.txt")
        val screenContentReader = screenInputStream?.bufferedReader().use { it?.readText() }
        val screenContent = screenContentReader?.replace("{{BlocName}}", blocClassName)
            ?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val blocInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/bloc/bloc_template.txt")
        val blocContentReader = blocInputStream?.bufferedReader().use { it?.readText() }
        val blocContent = blocContentReader?.replace("{{BlocName}}", blocClassName)
            ?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val stateInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/bloc/state_template.txt")
        val stateContentReader = stateInputStream?.bufferedReader().use { it?.readText() }
        val stateContent = stateContentReader?.replace("{{BlocName}}", blocClassName)
            ?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val project = event.project
        val directory = event.getData(PlatformDataKeys.VIRTUAL_FILE)

        if (directory != null) {
            WriteAction.run<Throwable> {

                var parentDirectory = directory
                if (userFolder) {
                    parentDirectory = directory.createChildDirectory(project, newFileName)
                }

                val blocDirection = parentDirectory.createChildDirectory(project, "bloc")

                val screenFile = parentDirectory.createChildData(event, "${newFileName}_screen.dart")
                screenFile.getOutputStream(project).use { it.write(screenContent.toByteArray()) }

                val blocFile = blocDirection.createChildData(event, "${newFileName}_bloc.dart")
                blocFile.getOutputStream(project).use { it.write(blocContent.toByteArray()) }

                val stateFile = blocDirection.createChildData(event, "${newFileName}_state.dart")
                stateFile.getOutputStream(project).use { it.write(stateContent.toByteArray()) }
                print("generate successfully!")
            }
        }
    }

}

class FileNameInputValidator : InputValidator {
    override fun checkInput(inputString: String): Boolean {
        return inputString.isNotEmpty() && inputString.matches(Regex("^[a-zA-Z0-9]+$"))
    }

    override fun canClose(inputString: String): Boolean {
        return checkInput(inputString)
    }
}