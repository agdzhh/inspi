package com.inspi.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home",    Icons.Outlined.Home,         Screen.Home.route),
    BottomNavItem("Gallery", Icons.Outlined.PhotoLibrary, Screen.Gallery.route),
    BottomNavItem("Coach",   Icons.Outlined.AutoAwesome,  Screen.Coach.route),
    BottomNavItem("Leagues", Icons.Outlined.EmojiEvents,  Screen.Friends.route),
    BottomNavItem("Profile", Icons.Outlined.Person,       Screen.Profile.route),
)

@Composable
fun InspiBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(tonalElevation = 8.dp) {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = false

                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
            )
        }
    }
}
