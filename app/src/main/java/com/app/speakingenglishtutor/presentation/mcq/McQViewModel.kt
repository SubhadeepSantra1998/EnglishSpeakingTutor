package com.app.speakingenglishtutor.presentation.mcq

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.domain.usecase.GetGrammarQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
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
    
    private var timerJob: Job? = null
    private val timerUpdateInterval = 100L // Update timer every 100ms for smooth animation

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
            is McqEvent.StartTimer -> startTimer()
            is McqEvent.PauseTimer -> pauseTimer()
            is McqEvent.ResumeTimer -> resumeTimer()
            is McqEvent.UpdateTimerProgress -> updateTimerProgress(event.progress)
            is McqEvent.TimerExpired -> handleTimerExpired()
            is McqEvent.ShowFeedbackDialog -> showFeedbackDialog()
            is McqEvent.HideFeedbackDialog -> hideFeedbackDialog()
        }
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            pauseTimer() // Stop any running timer
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
                                showFeedback = false,
                                timerProgress = 1f,
                                isTimerRunning = false,
                                timerExpired = false
                            )
                        }
                        // Start the timer for the first question
                        startTimer()
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
            pauseTimer() // Stop any running timer
            _uiState.update { it.copy(selectedDifficulty = difficulty) }
            loadQuestions()
        }
    }

    private fun selectAnswer(option: String) {
        if (!_uiState.value.showFeedback) {
            val currentQuestion = _uiState.value.currentQuestion
            
            if (currentQuestion != null) {
                // Pause the timer when an answer is selected
                pauseTimer()
                
                // Update the state - just set the selectedOption and showFeedback
                // isCorrectAnswer and currentFeedback are computed properties in McqUiState
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedOption = option,
                        showFeedback = true
                    )
                }
                
                // Show the feedback dialog
                showFeedbackDialog()
            }
        }
    }

    private fun moveToNextQuestion() {
        val currentIndex = _uiState.value.currentQuestionIndex
        val questionsSize = _uiState.value.questions.size
        
        // Hide the feedback dialog first
        hideFeedbackDialog()
        
        if (currentIndex < questionsSize - 1) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = currentIndex + 1,
                    selectedOption = null,
                    showFeedback = false,
                    timerProgress = 1f,
                    timerExpired = false
                )
            }
            // Start the timer for the next question
            startTimer()
        }
    }

    private fun resetFeedback() {
        _uiState.update { it.copy(showFeedback = false) }
    }

    private fun restartQuiz() {
        pauseTimer() // Stop any running timer
        
        // Hide the feedback dialog first
        hideFeedbackDialog()
        
        _uiState.update {
            it.copy(
                currentQuestionIndex = 0,
                selectedOption = null,
                showFeedback = false,
                timerProgress = 1f,
                timerExpired = false
            )
        }
        // Start the timer for the first question
        startTimer()
    }
    
    private fun startTimer() {
        // Cancel any existing timer job
        timerJob?.cancel()
        
        // Reset timer state
        _uiState.update { it.copy(timerProgress = 1f, isTimerRunning = true, timerExpired = false) }
        
        // Start a new timer job
        timerJob = viewModelScope.launch {
            val totalDuration = _uiState.value.questionTimerDuration
            val startTime = System.currentTimeMillis()
            var elapsedTime: Long
            
            while (true) {
                // Check if the coroutine is still active
                try {
                    ensureActive()
                } catch (e: Exception) {
                    // Coroutine was cancelled
                    break
                }
                
                elapsedTime = System.currentTimeMillis() - startTime
                
                if (elapsedTime >= totalDuration) {
                    // Timer expired
                    _uiState.update { it.copy(timerProgress = 0f, isTimerRunning = false, timerExpired = true) }
                    handleTimerExpired()
                    break
                }
                
                // Calculate and update progress (from 1.0 to 0.0)
                val progress = 1f - (elapsedTime.toFloat() / totalDuration.toFloat())
                _uiState.update { it.copy(timerProgress = progress) }
                
                try {
                    delay(timerUpdateInterval)
                } catch (e: Exception) {
                    // Delay was interrupted due to cancellation
                    break
                }
            }
        }
    }
    
    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { it.copy(isTimerRunning = false) }
    }
    
    private fun resumeTimer() {
        if (!_uiState.value.timerExpired && !_uiState.value.isTimerRunning) {
            // Only resume if the timer hasn't expired and is not already running
            startTimer()
        }
    }
    
    private fun updateTimerProgress(progress: Float) {
        _uiState.update { it.copy(timerProgress = progress) }
    }
    
    private fun handleTimerExpired() {
        // When the timer expires, we don't want to select any option
        // We just want to show the feedback dialog with the correct answer
        if (_uiState.value.selectedOption == null) {
            _uiState.update { 
                it.copy(
                    // Don't set selectedOption, just show the feedback
                    showFeedback = true,
                    timerExpired = true,
                    showFeedbackDialog = true
                )
            }
        } else {
            // If an answer was already selected, just make sure the dialog is shown
            showFeedbackDialog()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
    
    private fun showFeedbackDialog() {
        _uiState.update { it.copy(showFeedbackDialog = true) }
    }
    
    private fun hideFeedbackDialog() {
        _uiState.update { it.copy(showFeedbackDialog = false) }
    }
}