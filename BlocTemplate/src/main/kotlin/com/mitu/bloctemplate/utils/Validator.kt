package com.mitu.bloctemplate.utils
import com.intellij.openapi.ui.InputValidator

class FileNameInputValidator : InputValidator {
    override fun checkInput(inputString: String): Boolean {
        return inputString.isNotEmpty() && inputString.matches(Regex("^[a-zA-Z0-9_]+$"))
    }

    override fun canClose(inputString: String): Boolean {
        return checkInput(inputString)
    }
}