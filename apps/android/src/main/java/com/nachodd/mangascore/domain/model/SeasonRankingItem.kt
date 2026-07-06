package com.nachodd.mangascore.domain.model

data class SeasonRankingItem(
    val id: String,
    val seasonId: String,
    val participantId: String,
    val position: Int,
    val roundsCount: Int,
    val totalPoints: Int,
    val totalWeightGrams: Int,
    val topThreeCount: Int = 0,
    val biggestCatchWeightGrams: Int? = null,
)
