package com.nachodd.mangascore.data.repository

import com.nachodd.mangascore.data.local.database.AppDatabase
import com.nachodd.mangascore.data.mapper.ParticipantMapper
import com.nachodd.mangascore.data.mapper.RoundMapper
import com.nachodd.mangascore.data.mapper.SeasonMapper
import com.nachodd.mangascore.data.mapper.TournamentMapper
import com.nachodd.mangascore.domain.model.EventStatus
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.model.ScoringType
import com.nachodd.mangascore.domain.model.Season
import com.nachodd.mangascore.domain.model.SyncStatus
import com.nachodd.mangascore.domain.model.Tournament
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class LocalMangaScoreRepository @Inject constructor(
    private val database: AppDatabase,
) {
    fun observeSeasons(): Flow<List<Season>> =
        database.seasonDao()
            .observeByClub(DEFAULT_CLUB_ID)
            .map { seasons -> seasons.map(SeasonMapper::toDomain) }

    suspend fun getSeason(seasonId: String): Season? =
        database.seasonDao().getById(seasonId)?.let(SeasonMapper::toDomain)

    suspend fun createSeason(
        name: String,
        startsAt: Long,
        discardWorstRounds: Int,
    ) {
        val now = System.currentTimeMillis()
        database.seasonDao().upsert(
            SeasonMapper.toEntity(
                Season(
                    id = UUID.randomUUID().toString(),
                    clubId = DEFAULT_CLUB_ID,
                    name = name.trim(),
                    startsAt = startsAt,
                    createdAt = now,
                    updatedAt = now,
                    status = EventStatus.SCHEDULED,
                    scoringType = ScoringType.POINTS_BY_POSITION,
                    syncStatus = SyncStatus.PENDING_CREATE,
                    discardWorstRounds = discardWorstRounds,
                ),
            ),
        )
    }

    fun observeRounds(seasonId: String): Flow<List<Round>> =
        database.roundDao()
            .observeBySeason(seasonId)
            .map { rounds -> rounds.map(RoundMapper::toDomain) }

    suspend fun createRound(
        seasonId: String,
        name: String,
        roundNumber: Int?,
        location: String?,
    ) {
        val now = System.currentTimeMillis()
        val season = getSeason(seasonId)
        database.roundDao().upsert(
            RoundMapper.toEntity(
                Round(
                    id = UUID.randomUUID().toString(),
                    seasonId = seasonId,
                    clubId = season?.clubId ?: DEFAULT_CLUB_ID,
                    name = buildRoundName(name = name, roundNumber = roundNumber, location = location),
                    scheduledAt = now,
                    createdAt = now,
                    updatedAt = now,
                    status = EventStatus.SCHEDULED,
                    scoringType = ScoringType.TOTAL_WEIGHT,
                    syncStatus = SyncStatus.PENDING_CREATE,
                ),
            ),
        )
    }

    fun observeTournaments(): Flow<List<Tournament>> =
        database.tournamentDao()
            .observeByClub(DEFAULT_CLUB_ID)
            .map { tournaments -> tournaments.map(TournamentMapper::toDomain) }

    suspend fun getTournament(tournamentId: String): Tournament? =
        database.tournamentDao().getById(tournamentId)?.let(TournamentMapper::toDomain)

    suspend fun createTournament(
        name: String,
        location: String?,
    ) {
        val now = System.currentTimeMillis()
        database.tournamentDao().upsert(
            TournamentMapper.toEntity(
                Tournament(
                    id = UUID.randomUUID().toString(),
                    clubId = DEFAULT_CLUB_ID,
                    name = buildNameWithLocation(name = name, location = location),
                    scheduledAt = now,
                    createdAt = now,
                    updatedAt = now,
                    status = EventStatus.SCHEDULED,
                    scoringType = ScoringType.TOTAL_WEIGHT,
                    syncStatus = SyncStatus.PENDING_CREATE,
                ),
            ),
        )
    }

    fun observeParticipants(): Flow<List<Participant>> =
        database.participantDao()
            .observeByClub(DEFAULT_CLUB_ID)
            .map { participants -> participants.map(ParticipantMapper::toDomain) }

    suspend fun createParticipant(
        fullName: String,
        alias: String?,
        licenseNumber: String?,
    ) {
        val now = System.currentTimeMillis()
        database.participantDao().upsert(
            ParticipantMapper.toEntity(
                Participant(
                    id = UUID.randomUUID().toString(),
                    clubId = DEFAULT_CLUB_ID,
                    fullName = fullName.trim(),
                    createdAt = now,
                    updatedAt = now,
                    alias = alias?.trim()?.takeIf(String::isNotBlank),
                    licenseNumber = licenseNumber?.trim()?.takeIf(String::isNotBlank),
                    syncStatus = SyncStatus.PENDING_CREATE,
                ),
            ),
        )
    }

    fun observePendingSyncCount(): Flow<Int> =
        database.catchDao()
            .observePendingSync()
            .map { catches -> catches.size }

    private fun buildRoundName(
        name: String,
        roundNumber: Int?,
        location: String?,
    ): String {
        val baseName = name.trim().ifBlank { "Manga" }
        val numberedName = roundNumber?.let { "Manga $it - $baseName" } ?: baseName
        return buildNameWithLocation(numberedName, location)
    }

    private fun buildNameWithLocation(
        name: String,
        location: String?,
    ): String {
        val trimmedName = name.trim()
        val trimmedLocation = location?.trim().orEmpty()
        return if (trimmedLocation.isBlank()) trimmedName else "$trimmedName - $trimmedLocation"
    }

    companion object {
        const val DEFAULT_CLUB_ID = "local-club"
    }
}
