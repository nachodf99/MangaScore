package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.RankingItem

class RankingCalculator {
    fun calculateRanking(
        catches: List<Catch>,
        participants: List<Participant>,
        competitionType: CompetitionType,
        roundId: String? = null,
        tournamentId: String? = null,
    ): List<RankingItem> {
        val catchesByParticipant = catches.groupBy { it.participantId }

        return participants
            .map { participant ->
                val participantCatches = catchesByParticipant[participant.id].orEmpty()

                ParticipantRankingStats(
                    participantId = participant.id,
                    catchCount = participantCatches.size,
                    totalWeightGrams = participantCatches.sumOf { it.weightGrams },
                    biggestCatchWeightGrams = participantCatches.maxOfOrNull { it.weightGrams },
                )
            }
            .sortedWith(
                compareByDescending<ParticipantRankingStats> { it.totalWeightGrams }
                    .thenByDescending { it.biggestCatchWeightGrams ?: 0 }
                    .thenByDescending { it.catchCount }
                    .thenBy { it.participantId },
            )
            .mapIndexed { index, stats ->
                RankingItem(
                    id = buildRankingItemId(
                        competitionType = competitionType,
                        participantId = stats.participantId,
                        roundId = roundId,
                        tournamentId = tournamentId,
                    ),
                    competitionType = competitionType,
                    participantId = stats.participantId,
                    position = index + 1,
                    catchCount = stats.catchCount,
                    totalWeightGrams = stats.totalWeightGrams,
                    roundId = roundId,
                    tournamentId = tournamentId,
                    biggestCatchWeightGrams = stats.biggestCatchWeightGrams,
                )
            }
    }

    private fun buildRankingItemId(
        competitionType: CompetitionType,
        participantId: String,
        roundId: String?,
        tournamentId: String?,
    ): String {
        val competitionId = roundId ?: tournamentId ?: "all"
        return "${competitionType.name.lowercase()}-$competitionId-$participantId"
    }

    private data class ParticipantRankingStats(
        val participantId: String,
        val catchCount: Int,
        val totalWeightGrams: Int,
        val biggestCatchWeightGrams: Int?,
    )
}
