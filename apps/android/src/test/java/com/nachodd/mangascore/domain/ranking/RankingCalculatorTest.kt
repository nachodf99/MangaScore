package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Participant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RankingCalculatorTest {
    private val calculator = RankingCalculator()

    @Test
    fun `calculates ranking by total weight`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 1_200),
                catchEntry(id = "catch-2", participantId = "participant-1", weightGrams = 800),
                catchEntry(id = "catch-3", participantId = "participant-2", weightGrams = 1_500),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
        )

        assertEquals(listOf("participant-1", "participant-2"), ranking.map { it.participantId })
        assertEquals(2_000, ranking[0].totalWeightGrams)
        assertEquals(1_500, ranking[1].totalWeightGrams)
        assertEquals(listOf(1, 2), ranking.map { it.position })
    }

    @Test
    fun `resolves tie by biggest catch`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 700),
                catchEntry(id = "catch-2", participantId = "participant-1", weightGrams = 300),
                catchEntry(id = "catch-3", participantId = "participant-2", weightGrams = 600),
                catchEntry(id = "catch-4", participantId = "participant-2", weightGrams = 400),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
        )

        assertEquals("participant-1", ranking[0].participantId)
        assertEquals(700, ranking[0].biggestCatchWeightGrams)
        assertEquals("participant-2", ranking[1].participantId)
    }

    @Test
    fun `resolves tie by catch count`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 500),
                catchEntry(id = "catch-2", participantId = "participant-1", weightGrams = 500),
                catchEntry(id = "catch-3", participantId = "participant-2", weightGrams = 500),
                catchEntry(id = "catch-4", participantId = "participant-2", weightGrams = 250),
                catchEntry(id = "catch-5", participantId = "participant-2", weightGrams = 250),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
        )

        assertEquals("participant-2", ranking[0].participantId)
        assertEquals(3, ranking[0].catchCount)
        assertEquals("participant-1", ranking[1].participantId)
    }

    @Test
    fun `supports top three using take`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 4_000),
                catchEntry(id = "catch-2", participantId = "participant-2", weightGrams = 3_000),
                catchEntry(id = "catch-3", participantId = "participant-3", weightGrams = 2_000),
                catchEntry(id = "catch-4", participantId = "participant-4", weightGrams = 1_000),
            ),
            participants = participants(
                "participant-1",
                "participant-2",
                "participant-3",
                "participant-4",
            ),
            competitionType = CompetitionType.TOURNAMENT,
        )

        assertEquals(
            listOf("participant-1", "participant-2", "participant-3"),
            ranking.take(3).map { it.participantId },
        )
    }

    @Test
    fun `includes participant without catches at the end`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 900),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
        )

        val participantWithoutCatches = ranking.last()

        assertEquals("participant-2", participantWithoutCatches.participantId)
        assertEquals(0, participantWithoutCatches.totalWeightGrams)
        assertEquals(0, participantWithoutCatches.catchCount)
        assertNull(participantWithoutCatches.biggestCatchWeightGrams)
    }

    @Test
    fun `creates round ranking items`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 900),
            ),
            participants = participants("participant-1"),
            competitionType = CompetitionType.ROUND,
            roundId = "round-1",
        )

        assertEquals(CompetitionType.ROUND, ranking.single().competitionType)
        assertEquals("round-1", ranking.single().roundId)
        assertNull(ranking.single().tournamentId)
        assertEquals("round-round-1-participant-1", ranking.single().id)
    }

    @Test
    fun `creates tournament ranking items`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(id = "catch-1", participantId = "participant-1", weightGrams = 900),
            ),
            participants = participants("participant-1"),
            competitionType = CompetitionType.TOURNAMENT,
            tournamentId = "tournament-1",
        )

        assertEquals(CompetitionType.TOURNAMENT, ranking.single().competitionType)
        assertEquals("tournament-1", ranking.single().tournamentId)
        assertNull(ranking.single().roundId)
        assertEquals("tournament-tournament-1-participant-1", ranking.single().id)
    }

    @Test
    fun `round ranking ignores catches from other rounds`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    roundId = "round-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    roundId = "round-2",
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
            roundId = "round-1",
        )

        assertEquals("participant-1", ranking[0].participantId)
        assertEquals(1_000, ranking[0].totalWeightGrams)
        assertEquals("participant-2", ranking[1].participantId)
        assertEquals(0, ranking[1].totalWeightGrams)
    }

    @Test
    fun `tournament ranking ignores catches from other tournaments`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    tournamentId = "tournament-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    tournamentId = "tournament-2",
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.TOURNAMENT,
            tournamentId = "tournament-1",
        )

        assertEquals("participant-1", ranking[0].participantId)
        assertEquals(1_000, ranking[0].totalWeightGrams)
        assertEquals("participant-2", ranking[1].participantId)
        assertEquals(0, ranking[1].totalWeightGrams)
    }

    @Test
    fun `round ranking ignores tournament catches`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    roundId = "round-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    tournamentId = "tournament-1",
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
            roundId = "round-1",
        )

        assertEquals("participant-1", ranking[0].participantId)
        assertEquals(1_000, ranking[0].totalWeightGrams)
        assertEquals("participant-2", ranking[1].participantId)
        assertEquals(0, ranking[1].totalWeightGrams)
    }

    @Test
    fun `tournament ranking ignores round catches`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    tournamentId = "tournament-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    roundId = "round-1",
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.TOURNAMENT,
            tournamentId = "tournament-1",
        )

        assertEquals("participant-1", ranking[0].participantId)
        assertEquals(1_000, ranking[0].totalWeightGrams)
        assertEquals("participant-2", ranking[1].participantId)
        assertEquals(0, ranking[1].totalWeightGrams)
    }

    @Test
    fun `participants without catches stay at the end after filtering`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    roundId = "round-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    roundId = "round-2",
                ),
            ),
            participants = participants("participant-1", "participant-2", "participant-3"),
            competitionType = CompetitionType.ROUND,
            roundId = "round-1",
        )

        assertEquals(listOf("participant-1", "participant-2", "participant-3"), ranking.map { it.participantId })
        assertEquals(listOf(1_000, 0, 0), ranking.map { it.totalWeightGrams })
        assertEquals(listOf(1, 0, 0), ranking.map { it.catchCount })
    }

    @Test
    fun `ranking without competition id uses all received catches`() {
        val ranking = calculator.calculateRanking(
            catches = listOf(
                catchEntry(
                    id = "catch-1",
                    participantId = "participant-1",
                    weightGrams = 1_000,
                    roundId = "round-1",
                ),
                catchEntry(
                    id = "catch-2",
                    participantId = "participant-2",
                    weightGrams = 5_000,
                    tournamentId = "tournament-1",
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            competitionType = CompetitionType.ROUND,
        )

        assertEquals(listOf("participant-2", "participant-1"), ranking.map { it.participantId })
        assertEquals(listOf(5_000, 1_000), ranking.map { it.totalWeightGrams })
    }

    private fun participants(vararg ids: String): List<Participant> =
        ids.map { id ->
            Participant(
                id = id,
                clubId = "club-1",
                fullName = "Participant $id",
                createdAt = 1L,
                updatedAt = 1L,
            )
        }

    private fun catchEntry(
        id: String,
        participantId: String,
        weightGrams: Int,
        roundId: String? = null,
        tournamentId: String? = null,
    ): Catch =
        Catch(
            id = id,
            participantId = participantId,
            clubId = "club-1",
            weightGrams = weightGrams,
            caughtAt = 1L,
            createdAt = 1L,
            updatedAt = 1L,
            roundId = roundId,
            tournamentId = tournamentId,
        )
}
