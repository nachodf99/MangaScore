package com.nachodd.mangascore.domain.model

data class RankingItem(
    val id: String,
    val competitionType: CompetitionType,
    val participantId: String,
    val position: Int,
    val catchCount: Int,
    val totalWeightGrams: Int,
    val roundId: String? = null,
    val tournamentId: String? = null,
    val biggestCatchWeightGrams: Int? = null,
    val points: Int = 0,
)
