package com.nachodd.mangascore.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nachodd.mangascore.presentation.home.HomeScreen
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import com.nachodd.mangascore.presentation.participants.ParticipantsScreen
import com.nachodd.mangascore.presentation.seasons.SeasonDetailScreen
import com.nachodd.mangascore.presentation.seasons.SeasonRankingScreen
import com.nachodd.mangascore.presentation.seasons.SeasonsScreen
import com.nachodd.mangascore.presentation.sync.SyncStatusScreen
import com.nachodd.mangascore.presentation.tournaments.TournamentDetailScreen
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
            SeasonsScreen(
                onSeasonClick = { seasonId ->
                    navController.navigate(MangaScoreRoute.SeasonDetail.createRoute(seasonId))
                },
            )
        }
        composable(
            route = MangaScoreRoute.SeasonDetail.route,
            arguments = listOf(
                navArgument(MangaScoreRoute.SeasonDetail.SEASON_ID_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            SeasonDetailScreen(
                onSeasonRankingClick = { seasonId ->
                    navController.navigate(MangaScoreRoute.SeasonRanking.createRoute(seasonId))
                },
            )
        }
        composable(
            route = MangaScoreRoute.SeasonRanking.route,
            arguments = listOf(
                navArgument(MangaScoreRoute.SeasonRanking.SEASON_ID_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            SeasonRankingScreen()
        }
        composable(MangaScoreRoute.Tournaments.route) {
            TournamentsScreen(
                onTournamentClick = { tournamentId ->
                    navController.navigate(MangaScoreRoute.TournamentDetail.createRoute(tournamentId))
                },
            )
        }
        composable(
            route = MangaScoreRoute.TournamentDetail.route,
            arguments = listOf(
                navArgument(MangaScoreRoute.TournamentDetail.TOURNAMENT_ID_ARG) {
                    type = NavType.StringType
                },
            ),
        ) {
            TournamentDetailScreen()
        }
        composable(MangaScoreRoute.Participants.route) {
            ParticipantsScreen()
        }
        composable(MangaScoreRoute.SyncStatus.route) {
            SyncStatusScreen()
        }
    }
}
