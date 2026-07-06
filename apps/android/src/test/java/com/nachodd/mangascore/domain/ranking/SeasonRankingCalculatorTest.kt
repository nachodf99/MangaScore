package com.nachodd.mangascore.domain.ranking

import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.RankingItem
import com.nachodd.mangascore.domain.model.Round
import org.junit.Assert.assertEquals
import org.junit.Test

class SeasonRankingCalculatorTest {
    private val calculator = SeasonRankingCalculator()

    @Test
    fun `calculates season ranking without discards`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1", "round-2"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 1, totalWeightGrams = 1_000),
                    rankingItem(participantId = "participant-2", position = 2, totalWeightGrams = 800),
                ),
                "round-2" to listOf(
                    rankingItem(participantId = "participant-1", position = 2, totalWeightGrams = 700),
                    rankingItem(participantId = "participant-2", position = 1, totalWeightGrams = 900),
                ),
            ),
            participants = participants("participant-1", "participant-2"),
        )

        assertEquals(listOf("participant-1", "participant-2"), ranking.map { it.participantId })
        assertEquals(listOf(45, 45), ranking.map { it.totalPoints })
        assertEquals(listOf(1_700, 1_700), ranking.map { it.totalWeightGrams })
        assertEquals(listOf(2, 2), ranking.map { it.roundsCount })
    }

    @Test
    fun `assigns points by position`() {
        assertEquals(25, pointsForPosition(1, defaultPointsByPosition()))
        assertEquals(20, pointsForPosition(2, defaultPointsByPosition()))
        assertEquals(18, pointsForPosition(3, defaultPointsByPosition()))
        assertEquals(4, pointsForPosition(10, defaultPointsByPosition()))
        assertEquals(1, pointsForPosition(11, defaultPointsByPosition()))
    }

    @Test
    fun `orders by total points`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 2, totalWeightGrams = 2_000),
                    rankingItem(participantId = "participant-2", position = 1, totalWeightGrams = 1_000),
                ),
            ),
            participants = participants("participant-1", "participant-2"),
        )

        assertEquals(listOf("participant-2", "participant-1"), ranking.map { it.participantId })
        assertEquals(listOf(25, 20), ranking.map { it.totalPoints })
    }

    @Test
    fun `breaks points tie by total weight`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 1, totalWeightGrams = 1_500),
                    rankingItem(participantId = "participant-2", position = 1, totalWeightGrams = 2_000),
                ),
            ),
            participants = participants("participant-1", "participant-2"),
        )

        assertEquals(listOf("participant-2", "participant-1"), ranking.map { it.participantId })
    }

    @Test
    fun `breaks points and weight tie by top three count`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1", "round-2"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 1, totalWeightGrams = 1_000),
                    rankingItem(participantId = "participant-2", position = 4, totalWeightGrams = 1_000),
                ),
                "round-2" to listOf(
                    rankingItem(participantId = "participant-1", position = 3, totalWeightGrams = 1_000),
                    rankingItem(participantId = "participant-2", position = 1, totalWeightGrams = 1_000),
                ),
            ),
            participants = participants("participant-1", "participant-2"),
            pointsByPosition = mapOf(1 to 10, 3 to 10, 4 to 10),
        )

        assertEquals(listOf("participant-1", "participant-2"), ranking.map { it.participantId })
        assertEquals(listOf(2, 1), ranking.map { it.topThreeCount })
    }

    @Test
    fun `participant without results appears at the end`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 1, totalWeightGrams = 1_000),
                ),
            ),
            participants = participants("participant-1", "participant-2"),
        )

        assertEquals("participant-2", ranking.last().participantId)
        assertEquals(0, ranking.last().totalPoints)
        assertEquals(0, ranking.last().roundsCount)
        assertEquals(0, ranking.last().totalWeightGrams)
    }

    @Test
    fun `discards worst round score`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1", "round-2", "round-3"),
            roundRankings = mapOf(
                "round-1" to listOf(rankingItem(participantId = "participant-1", position = 1)),
                "round-2" to listOf(rankingItem(participantId = "participant-1", position = 2)),
                "round-3" to listOf(rankingItem(participantId = "participant-1", position = 10)),
            ),
            participants = participants("participant-1"),
            discardWorstRounds = 1,
        )

        assertEquals(45, ranking.single().totalPoints)
        assertEquals(3, ranking.single().roundsCount)
    }

    @Test
    fun `does not discard rounds without result`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1", "round-2"),
            roundRankings = mapOf(
                "round-1" to listOf(rankingItem(participantId = "participant-1", position = 1)),
                "round-2" to emptyList(),
            ),
            participants = participants("participant-1"),
            discardWorstRounds = 1,
        )

        assertEquals(0, ranking.single().totalPoints)
        assertEquals(1, ranking.single().roundsCount)
    }

    @Test
    fun `assigns final positions starting at one`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-1", position = 3),
                    rankingItem(participantId = "participant-2", position = 2),
                    rankingItem(participantId = "participant-3", position = 1),
                ),
            ),
            participants = participants("participant-1", "participant-2", "participant-3"),
        )

        assertEquals(listOf(1, 2, 3), ranking.map { it.position })
        assertEquals(listOf("participant-3", "participant-2", "participant-1"), ranking.map { it.participantId })
    }

    @Test
    fun `uses participant id for stable ordering`() {
        val ranking = calculator.calculateSeasonRanking(
            seasonId = "season-1",
            rounds = rounds("round-1"),
            roundRankings = mapOf(
                "round-1" to listOf(
                    rankingItem(participantId = "participant-b", position = 1, totalWeightGrams = 1_000),
                    rankingItem(participantId = "participant-a", position = 1, totalWeightGrams = 1_000),
                ),
            ),
            participants = participants("participant-b", "participant-a"),
        )

        assertEquals(listOf("participant-a", "participant-b"), ranking.map { it.participantId })
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

    private fun rounds(vararg ids: String): List<Round> =
        ids.map { id ->
            Round(
                id = id,
                seasonId = "season-1",
                clubId = "club-1",
                name = "Round $id",
                scheduledAt = 1L,
                createdAt = 1L,
                updatedAt = 1L,
            )
        }

    private fun rankingItem(
        participantId: String,
        position: Int,
        totalWeightGrams: Int = 0,
        biggestCatchWeightGrams: Int? = null,
    ): RankingItem =
        RankingItem(
            id = "ranking-round-1-$participantId-$position",
            competitionType = CompetitionType.ROUND,
            participantId = participantId,
            position = position,
            catchCount = 1,
            totalWeightGrams = totalWeightGrams,
            roundId = "round-1",
            biggestCatchWeightGrams = biggestCatchWeightGrams,
        )
}
