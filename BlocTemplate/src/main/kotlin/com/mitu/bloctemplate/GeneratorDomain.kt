package com.mitu.bloctemplate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.ui.Messages
import com.mitu.bloctemplate.utils.*
import com.mitu.bloctemplate.utils.Utils.Companion.capitalizeWords


class GeneratorDomain : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val dialogResult = Messages.showInputDialogWithCheckBox(
            "Enter domain name", "Generate Domain", "Use folder",
            true, true, null, null, FileNameInputValidator()
        )

        generatorFileName(dialogResult.first, dialogResult.second, e)

    }

    private fun generatorFileName(inputString: String, userFolder: Boolean, event: AnActionEvent) {
        val fileName = inputString.formatFilename()
        val domainClassName = capitalizeWords(fileName)
        val newFileName = fileName.camelCaseToSnakeCase()

        val clientInputStream = javaClass.classLoader.getResourceAsStream("domain_template/client_template.txt")
        val clientContentReader = clientInputStream?.bufferedReader().use { it?.readText() }
        val clientContent = clientContentReader?.replace("{{DomainName}}", domainClassName)
            ?.replace("{{FileNamePrefix}}", newFileName) ?: ""

        val repositoryInputStream = javaClass.classLoader.getResourceAsStream("domain_template/repository_template.txt")
        val repositoryContentReader = repositoryInputStream?.bufferedReader().use { it?.readText() }
        val repositoryContent = repositoryContentReader?.replace("{{DomainName}}", domainClassName)
            ?.replace("{{FileNamePrefix}}", newFileName) ?: ""

        val serviceInputStream = javaClass.classLoader.getResourceAsStream("domain_template/service_template.txt")
        val serviceContentReader = serviceInputStream?.bufferedReader().use { it?.readText() }
        val serviceContent = serviceContentReader?.replace("{{DomainName}}", domainClassName)
            ?.replace("{{FileNamePrefix}}", newFileName) ?: ""

        val project = event.project
        val directory = event.getData(PlatformDataKeys.VIRTUAL_FILE)

        if (directory != null) {
            WriteAction.run<Throwable> {
                var parentDirectory = directory
                if (userFolder) {
                    parentDirectory = directory.createChildDirectory(project, newFileName)
                }

                val clientFile = parentDirectory.createChildData(event, "${newFileName}_client.dart")
                clientFile.getOutputStream(project).use { it.write(clientContent.toByteArray()) }

                val repositoryFile = parentDirectory.createChildData(event, "${newFileName}_repository.dart")
                repositoryFile.getOutputStream(project).use { it.write(repositoryContent.toByteArray()) }

                val serviceFile = parentDirectory.createChildData(event, "${newFileName}_service.dart")
                serviceFile.getOutputStream(project).use { it.write(serviceContent.toByteArray()) }

                print("generate successfully!")
            }
        }
    }


}
