package com.app.speakingenglishtutor.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.speakingenglishtutor.data.model.Difficulty

@Composable
fun TimerProgressIndicator(
    progress: Float,
    difficulty: Difficulty,
    modifier: Modifier = Modifier,
    height: Int = 8
) {
    // Animate progress changes for smooth transitions
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 100),
        label = "timer_progress"
    )
    
    // Determine color based on difficulty and progress
    val backgroundColor = Color(0xFFE0E0E0) // Light gray background
    
    // Get color based on difficulty and progress
    val progressColor = when {
        progress < 0.2f -> Color(0xFFE57373) // Red when time is running out
        difficulty == Difficulty.EASY -> Color(0xFF81C784) // Green for easy
        difficulty == Difficulty.MEDIUM -> Color(0xFFFFD54F) // Yellow for medium
        difficulty == Difficulty.HARD -> Color(0xFFFF8A65) // Orange for hard
        else -> Color(0xFF81C784) // Default to green
    }
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height.dp)
                .background(progressColor)
        )
    }
}


@Preview(name = "Easy Difficulty", showBackground = true)
@Composable
fun TimerProgressIndicatorEasyPreview() {
    TimerProgressIndicator(
        progress = 0.8f,
        difficulty = Difficulty.EASY
    )
}

@Preview(name = "Hard - Low Time", showBackground = true)
@Composable
fun TimerProgressIndicatorHardLowTimePreview() {
    TimerProgressIndicator(
        progress = 0.1f,
        difficulty = Difficulty.HARD
    )
}
