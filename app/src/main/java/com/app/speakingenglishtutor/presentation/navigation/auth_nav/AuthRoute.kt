package com.app.speakingenglishtutor.presentation.navigation.auth_nav

sealed class AuthRoute(val name: String) {
    data object SplashScreen : AuthRoute("SplashScreen")
    data object LoginScreen : AuthRoute("LoginScreen")
}