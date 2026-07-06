package com.nachodd.mangascore.presentation.navigation

sealed class MangaScoreRoute(val route: String) {
    data object Home : MangaScoreRoute("home")
    data object Seasons : MangaScoreRoute("seasons")
    data object SeasonDetail : MangaScoreRoute("seasons/{seasonId}") {
        const val SEASON_ID_ARG = "seasonId"

        fun createRoute(seasonId: String): String = "seasons/$seasonId"
    }
    data object SeasonRanking : MangaScoreRoute("seasons/{seasonId}/ranking") {
        const val SEASON_ID_ARG = "seasonId"

        fun createRoute(seasonId: String): String = "seasons/$seasonId/ranking"
    }
    data object RoundDetail : MangaScoreRoute("rounds/{roundId}") {
        const val ROUND_ID_ARG = "roundId"

        fun createRoute(roundId: String): String = "rounds/$roundId"
    }
    data object Tournaments : MangaScoreRoute("tournaments")
    data object TournamentDetail : MangaScoreRoute("tournaments/{tournamentId}") {
        const val TOURNAMENT_ID_ARG = "tournamentId"

        fun createRoute(tournamentId: String): String = "tournaments/$tournamentId"
    }
    data object Participants : MangaScoreRoute("participants")
    data object ParticipantDetail : MangaScoreRoute("participants/{participantId}") {
        const val PARTICIPANT_ID_ARG = "participantId"

        fun createRoute(participantId: String): String = "participants/$participantId"
    }
    data object SyncStatus : MangaScoreRoute("sync_status")
}
