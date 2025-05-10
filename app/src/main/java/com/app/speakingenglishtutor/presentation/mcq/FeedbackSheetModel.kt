package com.app.speakingenglishtutor.presentation.mcq

data class FeedbackSheetModel(
    val isVisible: Boolean = false,
    val isCorrect: Boolean = false,
    val feedbackText: String = "",
    val correctAnswer: String = "",
    val isTimeExpired: Boolean = false,
    val isLastQuestion: Boolean = false
)
