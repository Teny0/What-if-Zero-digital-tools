package com.example.data

import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isGenerating: Boolean = false,
    val isError: Boolean = false,
    val isBookmarked: Boolean = false
) {
    /**
     * Extracts blockquotes (lines starting with > or blocks within quotes)
     * so user can one-tap copy or listen to just the script.
     */
    val extractedScripts: List<String> by lazy {
        val scripts = mutableListOf<String>()
        val lines = content.lines()
        var currentBlock = StringBuilder()
        var inBlock = false

        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith(">")) {
                inBlock = true
                val cleanLine = trimmed.removePrefix(">").trim()
                if (currentBlock.isNotEmpty()) currentBlock.append("\n")
                currentBlock.append(cleanLine)
            } else if (inBlock) {
                if (trimmed.isEmpty()) {
                    if (currentBlock.isNotEmpty()) {
                        scripts.add(currentBlock.toString().trim('"', ' '))
                        currentBlock = StringBuilder()
                    }
                    inBlock = false
                } else {
                    currentBlock.append("\n").append(trimmed)
                }
            }
        }
        if (currentBlock.isNotEmpty()) {
            scripts.add(currentBlock.toString().trim('"', ' '))
        }
        scripts
    }
}

enum class MessageRole {
    USER,
    MODEL
}

data class TechniqueCategory(
    val id: Int,
    val title: String,
    val emoji: String,
    val subtitle: String,
    val description: String,
    val samplePrompts: List<String>,
    val quickOfflineTips: List<String>
)

data class SavedTactic(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val categoryName: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
