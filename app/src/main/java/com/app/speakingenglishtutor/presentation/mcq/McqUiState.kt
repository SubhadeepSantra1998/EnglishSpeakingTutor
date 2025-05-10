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
    val showFeedback: Boolean = false,
    val timerProgress: Float = 1f,  // 1f means 100% (full), 0f means 0% (empty)
    val isTimerRunning: Boolean = false,
    val timerExpired: Boolean = false,
    val showFeedbackDialog: Boolean = false
) {
    val currentQuestion: GrammarQuestion?
        get() = questions.getOrNull(currentQuestionIndex)
        
    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= questions.size - 1
        
    val isCorrectAnswer: Boolean
        get() = selectedOption == currentQuestion?.answer
        
    val currentFeedback: String?
        get() = currentQuestion?.let { question ->
            if (timerExpired && selectedOption == null) {
                // When timer expires and no option is selected, show feedback for the correct answer
                question.feedback[question.answer] ?: ""
            } else {
                selectedOption?.let { option -> 
                    question.feedback[option] ?: "" 
                }
            }
        }
        
    val questionTimerDuration: Long
        get() = when(selectedDifficulty) {
            Difficulty.EASY -> 30000L     // 30 seconds for easy questions
            Difficulty.MEDIUM -> 20000L   // 20 seconds for medium questions
            Difficulty.HARD -> 15000L     // 15 seconds for hard questions
        }
}