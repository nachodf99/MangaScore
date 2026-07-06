package com.nachodd.mangascore.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nachodd.mangascore.presentation.home.HomeScreen
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute

@Composable
fun MangaScoreApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MangaScoreRoute.Home.route,
    ) {
        composable(MangaScoreRoute.Home.route) {
            HomeScreen(
                onSeasonsClick = { navController.navigate(MangaScoreRoute.Seasons.route) },
                onOneDayTournamentsClick = { navController.navigate(MangaScoreRoute.OneDayTournaments.route) },
            )
        }
        composable(MangaScoreRoute.Seasons.route) {
            HomeScreen.Placeholder(title = "Temporadas")
        }
        composable(MangaScoreRoute.OneDayTournaments.route) {
            HomeScreen.Placeholder(title = "Torneos de un día")
        }
    }
}
