# English Speaking Tutor App

A mobile application that helps users improve their English grammar skills through interactive multiple-choice questions.

## Features

- Multiple-choice grammar questions with instant feedback
- Three difficulty levels: Easy, Medium, and Hard
- Detailed feedback for each answer option
- Progress tracking through questions
- Ability to restart the quiz

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
2. Answer the grammar questions by selecting the correct option
3. Receive instant feedback on your answer
4. Navigate to the next question or restart the quiz when finished
