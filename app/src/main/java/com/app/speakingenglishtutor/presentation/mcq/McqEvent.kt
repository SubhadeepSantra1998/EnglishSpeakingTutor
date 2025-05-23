package com.app.speakingenglishtutor.presentation.mcq

import com.app.speakingenglishtutor.data.model.Difficulty

sealed class McqEvent {
    data object LoadQuestions : McqEvent()
    data class SelectDifficulty(val difficulty: Difficulty) : McqEvent()
    data class SelectAnswer(val option: String) : McqEvent()
    data object NextQuestion : McqEvent()
    data object ResetFeedback : McqEvent()
    data object RestartQuiz : McqEvent()
    data object StartTimer : McqEvent()
    data object PauseTimer : McqEvent()
    data object ResumeTimer : McqEvent()
    data class UpdateTimerProgress(val progress: Float) : McqEvent()
    data object TimerExpired : McqEvent()
    data object ShowFeedbackDialog : McqEvent()
    data object HideFeedbackDialog : McqEvent()
}