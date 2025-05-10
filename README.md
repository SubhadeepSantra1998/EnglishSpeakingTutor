# English Speaking Tutor App

A mobile application that helps users improve their English grammar skills through interactive multiple-choice questions.

## Features

- Multiple-choice grammar questions with instant feedback
- Three difficulty levels: Easy, Medium, and Hard
- Detailed feedback for each answer option
- Progress tracking through questions
- Ability to restart the quiz
- Timer progress indicator for each question based on difficulty:
  - Easy: 30 seconds
  - Medium: 20 seconds
  - Hard: 15 seconds
- Interactive feedback bottom sheet that displays:
  - Whether the answer is correct or incorrect
  - The correct answer when incorrect
  - Detailed explanation
  - Navigation options to continue or restart

## Setup Instructions

1. Clone the repository
2. Add your Gemini API key to `local.properties`:
   ```
   GEMINI_API_KEY=your_api_key_here
   ```
3. Build and run the application

## Technology Stack

- Kotlin
- Jetpack Compose for UI
- MVVM Architecture
- Clean Architecture principles
- Hilt for dependency injection
- Gemini AI API for generating grammar questions

## Project Structure

The project follows Clean Architecture principles with MVVM pattern:

- **Data Layer**: Contains repositories and data models
- **Domain Layer**: Contains use cases
- **Presentation Layer**: Contains UI components, ViewModels, and UI state management

## How to Use

1. Select a difficulty level (Easy, Medium, or Hard)
2. Answer the grammar questions by selecting the correct option before the timer runs out
3. Receive instant feedback via a bottom sheet showing whether your answer was correct
4. If the timer expires before you select an answer, the feedback will show the correct answer
5. Use the buttons in the feedback bottom sheet to navigate to the next question or restart the quiz
