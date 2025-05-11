package com.app.speakingenglishtutor.presentation.navigation.auth_nav

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.app.speakingenglishtutor.presentation.login.LoginScreen
import com.app.speakingenglishtutor.presentation.navigation.GraphConstant
import com.app.speakingenglishtutor.presentation.splash.SplashScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    upPress: () -> Unit,
    showSnackBar: (String, Boolean) -> Unit
) {
    navigation(startDestination = AuthRoute.SplashScreen.name, route = GraphConstant.AUTH_GRAPH) {
        composable(AuthRoute.SplashScreen.name) {
            SplashScreen(onNavigateToMain = {
                navController.navigate(AuthRoute.LoginScreen.name) {
                    popUpTo(AuthRoute.SplashScreen.name) { inclusive = true }
                }
            })
        }
        
        composable(AuthRoute.LoginScreen.name) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(GraphConstant.HOME_GRAPH) {
                    popUpTo(GraphConstant.AUTH_GRAPH) { inclusive = true }
                }
            })
        }
    }
}