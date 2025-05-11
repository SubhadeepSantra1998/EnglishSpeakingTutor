package com.app.speakingenglishtutor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.speakingenglishtutor.presentation.navigation.GraphConstant
import com.app.speakingenglishtutor.presentation.navigation.auth_nav.authNavGraph
import com.app.speakingenglishtutor.presentation.navigation.home_nav.homeNavGraph
import com.app.speakingenglishtutor.presentation.ui.theme.SpeakingEnglishTutorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var isAppReady by mutableStateOf(false)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Keep the splash screen visible until the app is ready
        splashScreen.setKeepOnScreenCondition { !isAppReady }
        
        // Set app as ready after initialization
        lifecycleScope.launch {
            isAppReady = true
        }
        
        setContent {
            SpeakingEnglishTutorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val scope = rememberCoroutineScope()
                    
                    // Show snackbar function that can be passed to navigation graphs
                    val showSnackbar: (String, Boolean) -> Unit = { message, isError ->
                        scope.launch {
                            snackbarHostState.showSnackbar(message)
                        }
                    }
                    
                    // Main navigation host that contains all navigation graphs
                    NavHost(
                        navController = navController,
                        startDestination = GraphConstant.AUTH_GRAPH
                    ) {
                        // Auth graph (Splash and Login)
                        authNavGraph(
                            navController = navController,
                            upPress = { navController.navigateUp() },
                            showSnackBar = showSnackbar
                        )
                        
                        // Home graph (Dashboard, Questionnaire, Profile)
                        homeNavGraph(
                            navController = navController,
                            upPress = { navController.navigateUp() },
                            showSnackBar = showSnackbar
                        )
                    }
                }
            }
        }
    }
}