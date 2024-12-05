package com.mitu.bloctemplate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class GeneratorAction: AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val message = "Bloc name:"
        val result = Messages.showInputDialog(e.project, message, "Generate Bloc", null) ?: ""
        if (result.isEmpty()) {
            return
        }
        if (validateFileName(result)) {
            generatorFileName(result, e)
        } else {
            Messages.showMessageDialog(e.project, "Please enter a valid name! 🤬", "Generate Bloc", Messages.getErrorIcon())
        }
    }

    private fun validateFileName(fileName: String): Boolean {
        return fileName.matches(Regex("^[a-zA-Z0-9]+$"))
    }

    private fun capitalizeWords(input: String): String {
        return input.replace(Regex("\\b[a-z]")) { match ->
            match.value.uppercase()
        }
    }

    private fun generatorFileName(fileName: String, event: AnActionEvent) {
        val blocNameLowerCase = fileName.lowercase()
        val blocClassName = capitalizeWords(fileName)
        val newFileName = fileName.lowercase()

        val screenInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/screen_template.txt")
        val screenContentReaded = screenInputStream?.bufferedReader().use { it?.readText() }
        val screenContent = screenContentReaded?.replace("{{BlocName}}", blocClassName)?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val blocInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/bloc/bloc_template.txt")
        val blocContentReaded = blocInputStream?.bufferedReader().use { it?.readText() }
        val blocContent = blocContentReaded?.replace("{{BlocName}}", blocClassName)?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val stateInputStream = javaClass.classLoader.getResourceAsStream("bloc_template/bloc/state_template.txt")
        val stateContentReaded = stateInputStream?.bufferedReader().use { it?.readText() }
        val stateContent = stateContentReaded?.replace("{{BlocName}}", blocClassName)?.replace("{{BlocNameLowerCase}}", blocNameLowerCase) ?: ""

        val project = event.project
        val directory = event.getData(PlatformDataKeys.VIRTUAL_FILE)

        if (directory != null) {
            WriteAction.run<Throwable> {
                val parentDirectory = directory.createChildDirectory(project, newFileName)
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