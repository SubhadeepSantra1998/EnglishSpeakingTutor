package com.app.speakingenglishtutor.presentation.navigation.home_nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.app.speakingenglishtutor.presentation.home.HomeScreen
import com.app.speakingenglishtutor.presentation.mcq.McqScreen
import com.app.speakingenglishtutor.presentation.navigation.GraphConstant
import com.app.speakingenglishtutor.presentation.profile.ProfileScreen

@Composable
fun HomeGraph(navController: NavController) {
    val bottomNavController = rememberNavController()
    val bottomNavItems = listOf(HomeRoute.Dashboard, HomeRoute.Questionnaire, HomeRoute.Profile)
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                imageVector = if (currentDestination?.hierarchy?.any { it.route == screen.name } == true) {
                                    screen.selectedIcon
                                } else {
                                    screen.unselectedIcon
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.name } == true,
                        onClick = {
                            bottomNavController.navigate(screen.name) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = HomeRoute.Dashboard.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HomeRoute.Dashboard.name) {
                HomeScreen()
            }
            composable(HomeRoute.Questionnaire.name) {
                McqScreen()
            }
            composable(HomeRoute.Profile.name) {
                ProfileScreen()
            }
        }
    }
}

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    upPress: () -> Unit,
    showSnackBar: (String, Boolean) -> Unit
) {
    navigation(startDestination = "home_main_screen", route = GraphConstant.HOME_GRAPH) {
        composable(route = "home_main_screen") {
            HomeGraph(navController = navController)
        }
    }
}