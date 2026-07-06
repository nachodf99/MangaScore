package com.nachodd.mangascore.presentation.navigation

sealed class MangaScoreRoute(val route: String) {
    data object Home : MangaScoreRoute("home")
    data object Seasons : MangaScoreRoute("seasons")
    data object OneDayTournaments : MangaScoreRoute("one_day_tournaments")
}
