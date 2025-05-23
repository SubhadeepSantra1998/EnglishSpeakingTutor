package com.app.speakingenglishtutor.presentation.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.speakingenglishtutor.presentation.ui.theme.SpeakingEnglishTutorTheme

@Composable
fun HomeScreen() {
    HomeScreenContent()
}

@Composable
fun HomeScreenContent(
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SpeakingEnglishTutorTheme {
        HomeScreen()
    }
}
