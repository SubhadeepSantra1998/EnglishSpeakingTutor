package com.app.speakingenglishtutor.data.model

data class GrammarQuestion(
    val id: Int,
    val sentence: String,
    val options: Map<String, String>,
    val answer: String,
    val feedback: Map<String, String>
)

enum class Difficulty(val label: String) {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard")
}
