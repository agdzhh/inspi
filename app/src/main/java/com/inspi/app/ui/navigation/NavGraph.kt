package com.inspi.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.inspi.app.ui.coach.CoachScreen
import com.inspi.app.ui.gallery.GalleryScreen
import com.inspi.app.ui.hobbyselection.HobbySelectionScreen
import com.inspi.app.ui.home.HomeScreen
import com.inspi.app.ui.onboarding.OnboardingScreen
import com.inspi.app.ui.profile.ProfileScreen
import com.inspi.app.ui.taskcomplete.TaskCompleteScreen

sealed class Screen(val route: String) {
    object Onboarding      : Screen("onboarding")
    object HobbySelection  : Screen("hobby_selection")
    object Home            : Screen("home")
    object Gallery         : Screen("gallery")
    object Coach           : Screen("coach")
    object Profile         : Screen("profile")
    object TaskComplete    : Screen("task_complete")
}

@Composable
fun InspiNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.HobbySelection.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onHobbyAlreadySelected = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.HobbySelection.route) {
            HobbySelectionScreen(
                onContinue = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.HobbySelection.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onCompleteTask = {
                    navController.navigate(Screen.TaskComplete.route)
                }
            )
        }

        composable(Screen.TaskComplete.route) {
            TaskCompleteScreen(
                onSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Gallery.route) {
            GalleryScreen(navController = navController)
        }

        composable(Screen.Coach.route) {
            CoachScreen(navController = navController)
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                onChangeHobby = {
                    navController.navigate(Screen.HobbySelection.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
