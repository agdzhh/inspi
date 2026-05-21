package com.inspi.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.ui.coach.CoachScreen
import com.inspi.app.ui.friends.FriendProfileScreen
import com.inspi.app.ui.friends.FriendsScreen
import com.inspi.app.ui.gallery.GalleryScreen
import com.inspi.app.ui.hobbyselection.HobbySelectionScreen
import com.inspi.app.ui.home.HomeScreen
import com.inspi.app.ui.nickname.NicknameScreen
import com.inspi.app.ui.onboarding.OnboardingScreen
import com.inspi.app.ui.profile.ProfileScreen
import com.inspi.app.ui.taskcomplete.TaskCompleteScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed class Screen(val route: String) {
    object Onboarding      : Screen("onboarding")
    object Nickname        : Screen("nickname")
    object HobbySelection  : Screen("hobby_selection")
    object Home            : Screen("home")
    object Gallery         : Screen("gallery")
    object Coach           : Screen("coach")
    object Friends         : Screen("friends")
    object Profile         : Screen("profile")
    object TaskComplete    : Screen("task_complete")
    object CoachCritique   : Screen("coach_critique/{submissionId}") {
        fun withId(id: Long) = "coach_critique/$id"
    }
    object Retake          : Screen("retake/{retakeSubmissionId}") {
        fun withId(id: Long) = "retake/$id"
    }
    object FriendProfile   : Screen("friend_profile/{friendCode}") {
        fun withCode(code: String) = "friend_profile/$code"
    }
}

// ── Resolves the correct start destination before the first frame ──────────────
@HiltViewModel
class NavViewModel @Inject constructor(prefs: InspiPreferences) : ViewModel() {
    /**
     * null  = still reading DataStore (don't compose NavHost yet)
     * other = the resolved start route
     */
    val startDestination = prefs.isOnboardingComplete
        .map { complete -> if (complete) Screen.Home.route else Screen.Onboarding.route }
        .stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = null)
}

@Composable
fun InspiNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: NavViewModel = hiltViewModel(),
) {
    val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()

    // Wait until DataStore emits — prevents the onboarding flash on subsequent opens.
    // The Box keeps the background colour consistent during the brief async read.
    if (startDestination == null) {
        Box(Modifier.fillMaxSize())
        return
    }

    NavHost(
        navController = navController,
        startDestination = startDestination!!
    ) {
        // ── First-launch flow ──────────────────────────────────────────────────
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Screen.Nickname.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Nickname.route) {
            NicknameScreen(
                onContinue = {
                    navController.navigate(Screen.HobbySelection.route) {
                        popUpTo(Screen.Nickname.route) { inclusive = true }
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

        // ── Main screens ───────────────────────────────────────────────────────
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                onCompleteTask = {
                    navController.navigate(Screen.TaskComplete.route)
                },
                onRetakeTask = { retakeId ->
                    navController.navigate(Screen.Retake.withId(retakeId))
                },
            )
        }

        composable(Screen.TaskComplete.route) {
            TaskCompleteScreen(
                onSuccess = { submissionId ->
                    navController.navigate(Screen.CoachCritique.withId(submissionId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
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

        composable(Screen.Friends.route) {
            FriendsScreen(
                navController = navController,
                onFriendClick = { code ->
                    navController.navigate(Screen.FriendProfile.withCode(code))
                },
            )
        }

        composable(
            route = Screen.FriendProfile.route,
            arguments = listOf(navArgument("friendCode") { type = NavType.StringType }),
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("friendCode") ?: return@composable
            FriendProfileScreen(navController = navController, friendCode = code)
        }

        composable(
            route = Screen.CoachCritique.route,
            arguments = listOf(navArgument("submissionId") { type = NavType.LongType }),
        ) {
            CoachScreen(navController = navController)
        }

        composable(
            route = Screen.Retake.route,
            arguments = listOf(navArgument("retakeSubmissionId") { type = NavType.LongType }),
        ) {
            TaskCompleteScreen(
                onSuccess = { submissionId ->
                    navController.navigate(Screen.CoachCritique.withId(submissionId)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onBack = { navController.popBackStack() }
            )
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