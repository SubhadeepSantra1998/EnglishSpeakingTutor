package com.app.speakingenglishtutor.presentation.mcq

import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.data.model.GrammarQuestion

data class McqUiState(
    val isLoading: Boolean = false,
    val questions: List<GrammarQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedDifficulty: Difficulty = Difficulty.EASY,
    val selectedOption: String? = null,
    val error: String? = null,
    val showFeedback: Boolean = false
) {
    val currentQuestion: GrammarQuestion?
        get() = questions.getOrNull(currentQuestionIndex)
        
    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= questions.size - 1
        
    val isCorrectAnswer: Boolean
        get() = selectedOption == currentQuestion?.answer
        
    val currentFeedback: String?
        get() = selectedOption?.let { option -> currentQuestion?.feedback?.get(option) }
}