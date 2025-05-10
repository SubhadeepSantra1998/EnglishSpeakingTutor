package com.app.speakingenglishtutor.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.speakingenglishtutor.R
import com.app.speakingenglishtutor.data.model.Difficulty

@Composable
fun AppTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AppSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun QuestionCard(
    questionText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Text(
            text = questionText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OptionItem(
    optionKey: String,
    optionText: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    showFeedback: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        !showFeedback -> MaterialTheme.colorScheme.surface
        isSelected && isCorrect == true -> Color(0xFFD6F5D6) // Light green for correct
        isSelected && isCorrect == false -> Color(0xFFF5D6D6) // Light red for incorrect
        else -> MaterialTheme.colorScheme.surface
    }
    
    val borderColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onOptionSelected(optionKey) },
                enabled = !showFeedback,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary
                )
            )
            
            Text(
                text = "$optionKey. $optionText",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun FeedbackCard(
    feedbackText: String,
    isCorrect: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isCorrect) {
        Color(0xFFD6F5D6) // Light green for correct
    } else {
        Color(0xFFF5D6D6) // Light red for incorrect
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = feedbackText,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun DifficultySelector(
    selectedDifficulty: Difficulty,
    onDifficultySelected: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Difficulty.entries.forEach { difficulty ->
            OutlinedButton(
                onClick = { onDifficultySelected(difficulty) },
                modifier = Modifier.padding(horizontal = 4.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedDifficulty == difficulty) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (selectedDifficulty == difficulty) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            ) {
                Text(
                    text = when(difficulty) {
                        Difficulty.EASY -> stringResource(id = R.string.mcq_difficulty_easy)
                        Difficulty.MEDIUM -> stringResource(id = R.string.mcq_difficulty_medium)
                        Difficulty.HARD -> stringResource(id = R.string.mcq_difficulty_hard)
                    },
                    color = if (selectedDifficulty == difficulty) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}

@Composable
fun NavigationButtons(
    onNextClick: () -> Unit,
    onRestartClick: () -> Unit,
    showRestartButton: Boolean,
    enableNextButton: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (showRestartButton) {
            Button(
                onClick = onRestartClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.mcq_restart_quiz))
            }
        }
        
        if (enableNextButton) {
            Button(
                onClick = onNextClick,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(text = stringResource(id = R.string.mcq_next_question))
            }
        }
    }
}

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.mcq_loading),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ErrorMessage(
    errorText: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClick) {
            Text(text = stringResource(id = R.string.mcq_retry))
        }
    }
}

@Composable
fun QuestionCounter(
    currentIndex: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.mcq_question_counter, currentIndex + 1, totalQuestions),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
