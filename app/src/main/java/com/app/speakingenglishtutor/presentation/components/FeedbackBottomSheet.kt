package com.app.speakingenglishtutor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.speakingenglishtutor.R
import com.app.speakingenglishtutor.presentation.mcq.FeedbackSheetModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(
    feedbackModel: FeedbackSheetModel,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Create a non-dismissible sheet state
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { false } // Prevent dismissal by dragging
    )

    if (feedbackModel.isVisible) {
        ModalBottomSheet(
            onDismissRequest = { /* Do nothing to prevent dismiss on outside click */ },
            sheetState = sheetState,
            dragHandle = null, // Remove drag handle to visually indicate it can't be dragged
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier,
        ) {
            FeedbackSheetContent(
                feedbackModel = feedbackModel,
                onNextQuestion = onNextQuestion,
                onRestartQuiz = onRestartQuiz
            )
        }
    }
}

@Composable
private fun FeedbackSheetContent(
    feedbackModel: FeedbackSheetModel,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isCorrect = feedbackModel.isCorrect
    val isTimeExpired = feedbackModel.isTimeExpired

    val backgroundColor = if (isCorrect) {
        Color(0xFFD6F5D6) // Green
    } else {
        Color(0xFFF5D6D6) // Red
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Feedback container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // Icon
                Icon(
                    painter = painterResource(
                        id = if (isCorrect) R.drawable.ic_check_circle else R.drawable.ic_error
                    ),
                    contentDescription = if (isCorrect) "Correct" else "Incorrect",
                    tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFE57373),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isCorrect) {
                    // ✔️ Correct answer content
                    Text(
                        text = stringResource(R.string.mcq_feedback_correct),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )

                } else if (isTimeExpired) {
                    // ⏱ Time expired content (only correct answer)
                    Text(
                        text = stringResource(R.string.mcq_timer_expired),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE57373)
                    )

                } else {
                    // ❌ Incorrect: Show correct answer first, then the title
                    Text(
                        text = stringResource(R.string.mcq_feedback_incorrect),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE57373)
                    )

                    Text(
                        text = stringResource(
                            R.string.mcq_correct_answer,
                            feedbackModel.correctAnswer
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (isTimeExpired) {
            Text(
                text = stringResource(R.string.mcq_correct_answer, feedbackModel.correctAnswer),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
            )
        } else if (!isCorrect) {
            Text(
                text = feedbackModel.feedbackText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        // Buttons
        NavigationButtons(
            onNextClick = onNextQuestion,
            onRestartClick = onRestartQuiz,
            showRestartButton = feedbackModel.isLastQuestion,
            enableNextButton = !feedbackModel.isLastQuestion
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(name = "Correct Answer", showBackground = true)
@Composable
fun PreviewFeedbackCorrect() {
    FeedbackSheetContent(
        feedbackModel = FeedbackSheetModel(
            isCorrect = true,
            correctAnswer = "42",
            feedbackText = "Good job!",
            isLastQuestion = false
        ),
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}

@Preview(name = "Incorrect Answer", showBackground = true)
@Composable
fun PreviewFeedbackIncorrect() {
    FeedbackSheetContent(
        feedbackModel = FeedbackSheetModel(
            isCorrect = false,
            isTimeExpired = false,
            correctAnswer = "42",
            feedbackText = "Remember to read the question carefully.",
            isLastQuestion = false
        ),
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}

@Preview(name = "Time Expired", showBackground = true)
@Composable
fun PreviewFeedbackTimeExpired() {
    FeedbackSheetContent(
        feedbackModel = FeedbackSheetModel(
            isCorrect = false,
            isTimeExpired = true,
            correctAnswer = "42",
            feedbackText = "Try to manage your time better!",
            isLastQuestion = false
        ),
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}
