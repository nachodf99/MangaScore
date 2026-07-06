package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.Catch

class BiggestCatchCalculator {
    fun getBiggestCatchForRound(
        catches: List<Catch>,
        roundId: String,
    ): Catch? =
        getBiggestCatch(catches.filter { it.roundId == roundId })

    fun getBiggestCatchForTournament(
        catches: List<Catch>,
        tournamentId: String,
    ): Catch? =
        getBiggestCatch(catches.filter { it.tournamentId == tournamentId })

    fun getBiggestCatchForSeason(
        catches: List<Catch>,
        roundIds: Set<String>,
    ): Catch? =
        getBiggestCatch(catches.filter { it.roundId in roundIds })

    fun getBiggestCatch(catches: List<Catch>): Catch? =
        catches.minWithOrNull(
            compareByDescending<Catch> { it.weightGrams }
                .thenBy { it.caughtAt }
                .thenBy { it.id },
        )
}
