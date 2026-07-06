package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.Catch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class BiggestCatchCalculatorTest {
    private val calculator = BiggestCatchCalculator()

    @Test
    fun `gets biggest catch for round`() {
        val biggestCatch = calculator.getBiggestCatchForRound(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 900, roundId = "round-1"),
                catchEntry(id = "catch-2", weightGrams = 1_400, roundId = "round-1"),
                catchEntry(id = "catch-3", weightGrams = 2_000, roundId = "round-2"),
            ),
            roundId = "round-1",
        )

        assertEquals("catch-2", biggestCatch?.id)
    }

    @Test
    fun `gets biggest catch for tournament`() {
        val biggestCatch = calculator.getBiggestCatchForTournament(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 900, tournamentId = "tournament-1"),
                catchEntry(id = "catch-2", weightGrams = 1_400, tournamentId = "tournament-1"),
                catchEntry(id = "catch-3", weightGrams = 2_000, tournamentId = "tournament-2"),
            ),
            tournamentId = "tournament-1",
        )

        assertEquals("catch-2", biggestCatch?.id)
    }

    @Test
    fun `gets biggest catch for season`() {
        val biggestCatch = calculator.getBiggestCatchForSeason(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 900, roundId = "round-1"),
                catchEntry(id = "catch-2", weightGrams = 1_400, roundId = "round-2"),
                catchEntry(id = "catch-3", weightGrams = 2_000, roundId = "round-3"),
                catchEntry(id = "catch-4", weightGrams = 3_000, tournamentId = "tournament-1"),
            ),
            roundIds = setOf("round-1", "round-2"),
        )

        assertEquals("catch-2", biggestCatch?.id)
    }

    @Test
    fun `returns null for empty list`() {
        val biggestCatch = calculator.getBiggestCatch(emptyList())

        assertNull(biggestCatch)
    }

    @Test
    fun `round biggest catch ignores catches from other rounds`() {
        val biggestCatch = calculator.getBiggestCatchForRound(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 900, roundId = "round-1"),
                catchEntry(id = "catch-2", weightGrams = 3_000, roundId = "round-2"),
            ),
            roundId = "round-1",
        )

        assertEquals("catch-1", biggestCatch?.id)
    }

    @Test
    fun `tournament biggest catch ignores catches from other tournaments`() {
        val biggestCatch = calculator.getBiggestCatchForTournament(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 900, tournamentId = "tournament-1"),
                catchEntry(id = "catch-2", weightGrams = 3_000, tournamentId = "tournament-2"),
            ),
            tournamentId = "tournament-1",
        )

        assertEquals("catch-1", biggestCatch?.id)
    }

    @Test
    fun `tie by weight is resolved by oldest caught at`() {
        val biggestCatch = calculator.getBiggestCatch(
            catches = listOf(
                catchEntry(id = "catch-1", weightGrams = 1_500, caughtAt = 20L),
                catchEntry(id = "catch-2", weightGrams = 1_500, caughtAt = 10L),
            ),
        )

        assertEquals("catch-2", biggestCatch?.id)
    }

    @Test
    fun `full tie is resolved by id`() {
        val biggestCatch = calculator.getBiggestCatch(
            catches = listOf(
                catchEntry(id = "catch-b", weightGrams = 1_500, caughtAt = 10L),
                catchEntry(id = "catch-a", weightGrams = 1_500, caughtAt = 10L),
            ),
        )

        assertEquals("catch-a", biggestCatch?.id)
    }

    private fun catchEntry(
        id: String,
        weightGrams: Int,
        caughtAt: Long = 1L,
        roundId: String? = null,
        tournamentId: String? = null,
    ): Catch =
        Catch(
            id = id,
            participantId = "participant-1",
            clubId = "club-1",
            weightGrams = weightGrams,
            caughtAt = caughtAt,
            createdAt = 1L,
            updatedAt = 1L,
            roundId = roundId,
            tournamentId = tournamentId,
        )
}
