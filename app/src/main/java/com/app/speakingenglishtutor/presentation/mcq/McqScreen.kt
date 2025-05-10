package com.app.speakingenglishtutor.presentation.mcq

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.speakingenglishtutor.R
import com.app.speakingenglishtutor.data.model.Difficulty
import com.app.speakingenglishtutor.data.model.GrammarQuestion
import com.app.speakingenglishtutor.presentation.components.AppSubtitle
import com.app.speakingenglishtutor.presentation.components.AppTitle
import com.app.speakingenglishtutor.presentation.components.DifficultySelector
import com.app.speakingenglishtutor.presentation.components.ErrorMessage
import com.app.speakingenglishtutor.presentation.components.LoadingIndicator
import com.app.speakingenglishtutor.presentation.components.FeedbackBottomSheet
import com.app.speakingenglishtutor.presentation.components.OptionItem
import com.app.speakingenglishtutor.presentation.components.QuestionCard
import com.app.speakingenglishtutor.presentation.components.QuestionCounter
import com.app.speakingenglishtutor.presentation.components.TimerProgressIndicator
import com.app.speakingenglishtutor.presentation.ui.theme.SpeakingEnglishTutorTheme

@Composable
fun McqScreen(viewModel: McQViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    McqScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun McqScreenContent(
    uiState: McqUiState,
    onEvent: (McqEvent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AppTitle(title = stringResource(id = R.string.mcq_app_title))
            Spacer(modifier = Modifier.height(8.dp))
            AppSubtitle(subtitle = stringResource(id = R.string.mcq_subtitle))
            Spacer(modifier = Modifier.height(16.dp))
            
            // Timer progress indicator
            if (!uiState.isLoading && uiState.questions.isNotEmpty()) {
                TimerProgressIndicator(
                    progress = uiState.timerProgress,
                    difficulty = uiState.selectedDifficulty,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            DifficultySelector(
                selectedDifficulty = uiState.selectedDifficulty,
                onDifficultySelected = { difficulty ->
                    onEvent(McqEvent.SelectDifficulty(difficulty))
                }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            when {
                uiState.isLoading -> {
                    LoadingIndicator(modifier = Modifier.padding(top = 32.dp))
                }
                uiState.error != null -> {
                    ErrorMessage(
                        errorText = uiState.error,
                        onRetryClick = { onEvent(McqEvent.LoadQuestions) },
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
                uiState.questions.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AppSubtitle(subtitle = stringResource(id = R.string.mcq_no_questions))
                    }
                }
                else -> {
                    QuestionContent(
                        uiState = uiState,
                        onEvent = onEvent
                    )
                    
                    // Start the timer if it's not running and not showing feedback
                    if (!uiState.isTimerRunning && !uiState.showFeedback && !uiState.timerExpired) {
                        LaunchedEffect(uiState.currentQuestionIndex) {
                            onEvent(McqEvent.StartTimer)
                        }
                    }
                }
            }
        }
    }
    
    // Show feedback bottom sheet when needed
    val currentQuestion = uiState.currentQuestion
    if (uiState.showFeedbackDialog && currentQuestion != null) {
        // For timer expired case, we might not have a selected option
        val isCorrect = if (uiState.selectedOption != null) uiState.isCorrectAnswer else false
        val feedbackText = uiState.currentFeedback ?: ""
        val correctAnswer = currentQuestion.options[currentQuestion.answer] ?: ""
        
        // Create the feedback sheet model
        val feedbackModel = FeedbackSheetModel(
            isVisible = true,
            isCorrect = isCorrect,
            feedbackText = feedbackText,
            correctAnswer = correctAnswer,
            isTimeExpired = uiState.timerExpired,
            isLastQuestion = uiState.isLastQuestion
        )
        
        // Disable back button when feedback sheet is visible
        BackHandler(enabled = true) {
            // Do nothing - prevent back button from dismissing the sheet
            // User must use the provided buttons to proceed
        }
        
        FeedbackBottomSheet(
            feedbackModel = feedbackModel,
            onNextQuestion = {
                onEvent(McqEvent.HideFeedbackDialog)
                onEvent(McqEvent.NextQuestion)
            },
            onRestartQuiz = {
                onEvent(McqEvent.HideFeedbackDialog)
                onEvent(McqEvent.RestartQuiz)
            }
        )
    }
}

@Composable
fun QuestionContent(
    uiState: McqUiState,
    onEvent: (McqEvent) -> Unit
) {
    val currentQuestion = uiState.currentQuestion
    
    if (currentQuestion != null) {
        QuestionCounter(
            currentIndex = uiState.currentQuestionIndex,
            totalQuestions = uiState.questions.size
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        QuestionCard(questionText = currentQuestion.sentence)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Display options
        currentQuestion.options.forEach { (key, text) ->
            OptionItem(
                optionKey = key,
                optionText = text,
                isSelected = uiState.selectedOption == key,
                isCorrect = if (uiState.showFeedback) key == currentQuestion.answer else null,
                showFeedback = uiState.showFeedback,
                onOptionSelected = { onEvent(McqEvent.SelectAnswer(it)) }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun McqScreenPreview() {
    SpeakingEnglishTutorTheme {
        McqScreenContent(
            uiState = McqUiState(
                isLoading = false,
                questions = listOf(
                    GrammarQuestion(
                        id = 1,
                        sentence = "She ___ to the store yesterday.",
                        options = mapOf(
                            "A" to "go",
                            "B" to "goes",
                            "C" to "went",
                            "D" to "going"
                        ),
                        answer = "C",
                        feedback = mapOf(
                            "A" to "Incorrect. 'Go' is the present tense form.",
                            "B" to "Incorrect. 'Goes' is the third-person singular present tense form.",
                            "C" to "Correct! 'Went' is the past tense form of 'go'.",
                            "D" to "Incorrect. 'Going' is the present participle form."
                        )
                    )
                ),
                selectedDifficulty = Difficulty.EASY
            ),
            onEvent = {}
        )
    }
}
