package com.nachodd.mangascore.presentation.navigation

sealed class MangaScoreRoute(val route: String) {
    data object Home : MangaScoreRoute("home")
    data object Seasons : MangaScoreRoute("seasons")
    data object Tournaments : MangaScoreRoute("tournaments")
    data object Participants : MangaScoreRoute("participants")
    data object SyncStatus : MangaScoreRoute("sync_status")
}
