package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.RankingItem
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.model.SeasonRankingItem

class SeasonRankingCalculator {
    fun calculateSeasonRanking(
        seasonId: String,
        rounds: List<Round>,
        roundRankings: Map<String, List<RankingItem>>,
        participants: List<Participant>,
        discardWorstRounds: Int = 0,
        pointsByPosition: Map<Int, Int> = defaultPointsByPosition(),
    ): List<SeasonRankingItem> {
        val statsByParticipant = participants.associate { participant ->
            participant.id to ParticipantSeasonStats(participantId = participant.id)
        }

        rounds.forEach { round ->
            roundRankings[round.id].orEmpty().forEach { rankingItem ->
                val stats = statsByParticipant[rankingItem.participantId] ?: return@forEach
                val points = pointsForPosition(rankingItem.position, pointsByPosition)

                stats.roundPoints += points
                stats.roundsCount += 1
                stats.totalWeightGrams += rankingItem.totalWeightGrams
                if (rankingItem.position <= TOP_THREE_LIMIT) {
                    stats.topThreeCount += 1
                }
                stats.biggestCatchWeightGrams = maxOfNullable(
                    stats.biggestCatchWeightGrams,
                    rankingItem.biggestCatchWeightGrams,
                )
            }
        }

        return statsByParticipant.values
            .map { stats ->
                SeasonRankingStats(
                    participantId = stats.participantId,
                    totalPoints = stats.totalPointsAfterDiscards(discardWorstRounds),
                    roundsCount = stats.roundsCount,
                    totalWeightGrams = stats.totalWeightGrams,
                    topThreeCount = stats.topThreeCount,
                    biggestCatchWeightGrams = stats.biggestCatchWeightGrams,
                )
            }
            .sortedWith(
                compareByDescending<SeasonRankingStats> { it.totalPoints }
                    .thenByDescending { it.totalWeightGrams }
                    .thenByDescending { it.topThreeCount }
                    .thenBy { it.participantId },
            )
            .mapIndexed { index, stats ->
                SeasonRankingItem(
                    id = "season-$seasonId-${stats.participantId}",
                    seasonId = seasonId,
                    participantId = stats.participantId,
                    position = index + 1,
                    roundsCount = stats.roundsCount,
                    totalPoints = stats.totalPoints,
                    totalWeightGrams = stats.totalWeightGrams,
                    topThreeCount = stats.topThreeCount,
                    biggestCatchWeightGrams = stats.biggestCatchWeightGrams,
                )
            }
    }

    private data class ParticipantSeasonStats(
        val participantId: String,
        val roundPoints: MutableList<Int> = mutableListOf(),
        var roundsCount: Int = 0,
        var totalWeightGrams: Int = 0,
        var topThreeCount: Int = 0,
        var biggestCatchWeightGrams: Int? = null,
    ) {
        fun totalPointsAfterDiscards(discardWorstRounds: Int): Int {
            if (discardWorstRounds <= 0) {
                return roundPoints.sum()
            }

            return roundPoints
                .sorted()
                .drop(discardWorstRounds.coerceAtMost(roundPoints.size))
                .sum()
        }
    }

    private data class SeasonRankingStats(
        val participantId: String,
        val totalPoints: Int,
        val roundsCount: Int,
        val totalWeightGrams: Int,
        val topThreeCount: Int,
        val biggestCatchWeightGrams: Int?,
    )

    private fun maxOfNullable(first: Int?, second: Int?): Int? =
        when {
            first == null -> second
            second == null -> first
            else -> maxOf(first, second)
        }

    private companion object {
        const val TOP_THREE_LIMIT = 3
    }
}

fun defaultPointsByPosition(): Map<Int, Int> =
    mapOf(
        1 to 25,
        2 to 20,
        3 to 18,
        4 to 16,
        5 to 14,
        6 to 12,
        7 to 10,
        8 to 8,
        9 to 6,
        10 to 4,
    )

fun pointsForPosition(
    position: Int,
    pointsByPosition: Map<Int, Int>,
): Int =
    pointsByPosition[position] ?: 1
