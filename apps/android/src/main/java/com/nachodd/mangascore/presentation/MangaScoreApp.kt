package com.nachodd.mangascore.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nachodd.mangascore.presentation.home.HomeScreen
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import com.nachodd.mangascore.presentation.participants.ParticipantsScreen
import com.nachodd.mangascore.presentation.seasons.SeasonsScreen
import com.nachodd.mangascore.presentation.sync.SyncStatusScreen
import com.nachodd.mangascore.presentation.tournaments.TournamentsScreen

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
                onTournamentsClick = { navController.navigate(MangaScoreRoute.Tournaments.route) },
                onParticipantsClick = { navController.navigate(MangaScoreRoute.Participants.route) },
                onSyncClick = { navController.navigate(MangaScoreRoute.SyncStatus.route) },
            )
        }
        composable(MangaScoreRoute.Seasons.route) {
            SeasonsScreen()
        }
        composable(MangaScoreRoute.Tournaments.route) {
            TournamentsScreen()
        }
        composable(MangaScoreRoute.Participants.route) {
            ParticipantsScreen()
        }
        composable(MangaScoreRoute.SyncStatus.route) {
            SyncStatusScreen()
        }
    }
}
