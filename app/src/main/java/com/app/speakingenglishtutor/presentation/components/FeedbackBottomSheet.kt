package com.app.speakingenglishtutor.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
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
import androidx.compose.ui.unit.dp
import com.app.speakingenglishtutor.R
import com.app.speakingenglishtutor.presentation.mcq.FeedbackSheetModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackBottomSheet(
    feedbackModel: FeedbackSheetModel,
    onDismiss: () -> Unit,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    if (feedbackModel.isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = modifier
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
    val backgroundColor = if (feedbackModel.isCorrect) {
        Color(0xFFD6F5D6) // Light green for correct
    } else {
        Color(0xFFF5D6D6) // Light red for incorrect
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Feedback header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Feedback icon
                Icon(
                    painter = painterResource(
                        id = if (feedbackModel.isCorrect) R.drawable.ic_check_circle 
                        else R.drawable.ic_error
                    ),
                    contentDescription = if (feedbackModel.isCorrect) "Correct" else "Incorrect",
                    tint = if (feedbackModel.isCorrect) Color(0xFF4CAF50) else Color(0xFFE57373),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // Feedback title
                Text(
                    text = if (feedbackModel.isCorrect) stringResource(R.string.mcq_feedback_correct)
                          else if (feedbackModel.isTimeExpired) stringResource(R.string.mcq_timer_expired)
                          else stringResource(R.string.mcq_feedback_incorrect),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (feedbackModel.isCorrect) Color(0xFF4CAF50) else Color(0xFFE57373)
                )
                
                // Show correct answer if incorrect or time expired
                if (!feedbackModel.isCorrect) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.mcq_correct_answer, feedbackModel.correctAnswer),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Feedback text
        if (feedbackModel.feedbackText.isNotEmpty()) {
            Text(
                text = feedbackModel.feedbackText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Navigation buttons
        NavigationButtons(
            onNextClick = onNextQuestion,
            onRestartClick = onRestartQuiz,
            showRestartButton = feedbackModel.isLastQuestion,
            enableNextButton = !feedbackModel.isLastQuestion
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
