package com.mitu.bloctemplate.utils

import java.util.*

class Utils {
    companion object {

        fun capitalizeWords(input: String): String {
            return input.replace(Regex("\\b[a-z]")) { match ->
                match.value.uppercase()
            }
        }

        fun customFileName(originalString: String): String {
            val fileName = originalString
                .trim()
                .replace("\\s+".toRegex(), " ")
                .replace(" ", "_")
                .lowercase()
            return fileName
        }
    }
}

//MARK: Extensions
fun String.convertUpperCases(): String {
    val regex = "([A-Z])([A-Z]+)".toRegex()
    return regex.replace(this) {
        it.groups[1]!!.value + it.groups[2]!!.value.lowercase()
    }
}

fun String.formatFilename(): String {
    return this.split('_')
        .joinToString("") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }
}

fun String.camelCaseToSnakeCase(): String {
    val convertUpperCase = this.convertUpperCases()
    val abc = convertUpperCase
        .replace("([A-Z][a-z]+)".toRegex(), "_$1")
        .lowercase()
        .removePrefix("_")
    return  abc;
}
