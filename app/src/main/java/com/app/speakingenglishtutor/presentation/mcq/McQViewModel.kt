package com.app.speakingenglishtutor.presentation.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.domain.usecase.GetGrammarQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class McQViewModel @Inject constructor(
    private val getGrammarQuestionsUseCase: GetGrammarQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(McqUiState())
    val uiState: StateFlow<McqUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    fun onEvent(event: McqEvent) {
        when (event) {
            is McqEvent.LoadQuestions -> loadQuestions()
            is McqEvent.SelectDifficulty -> selectDifficulty(event.difficulty)
            is McqEvent.SelectAnswer -> selectAnswer(event.option)
            is McqEvent.NextQuestion -> moveToNextQuestion()
            is McqEvent.ResetFeedback -> resetFeedback()
            is McqEvent.RestartQuiz -> restartQuiz()
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            getGrammarQuestionsUseCase(_uiState.value.selectedDifficulty).collect { result ->
                result.fold(
                    onSuccess = { questions ->
                        _uiState.update {
                            it.copy(
                                questions = questions,
                                isLoading = false,
                                currentQuestionIndex = 0,
                                selectedOption = null,
                                showFeedback = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = error.message ?: "An error occurred while loading questions"
                            )
                        }
                    }
                )
            }
        }
    }

    private fun selectDifficulty(difficulty: Difficulty) {
        if (_uiState.value.selectedDifficulty != difficulty) {
            _uiState.update { it.copy(selectedDifficulty = difficulty) }
            loadQuestions()
        }
    }

    private fun selectAnswer(option: String) {
        if (!_uiState.value.showFeedback) {
            _uiState.update { it.copy(selectedOption = option, showFeedback = true) }
        }
    }

    private fun moveToNextQuestion() {
        val currentIndex = _uiState.value.currentQuestionIndex
        val questionsSize = _uiState.value.questions.size
        
        if (currentIndex < questionsSize - 1) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = currentIndex + 1,
                    selectedOption = null,
                    showFeedback = false
                )
            }
        }
    }

    private fun resetFeedback() {
        _uiState.update { it.copy(showFeedback = false) }
    }

    private fun restartQuiz() {
        _uiState.update {
            it.copy(
                currentQuestionIndex = 0,
                selectedOption = null,
                showFeedback = false
            )
        }
    }
}