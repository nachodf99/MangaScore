package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.TournamentEntity
import com.nachodd.mangascore.domain.model.EventStatus
import com.nachodd.mangascore.domain.model.ScoringType
import com.nachodd.mangascore.domain.model.SyncStatus
import com.nachodd.mangascore.domain.model.Tournament

object TournamentMapper {
    fun toDomain(entity: TournamentEntity): Tournament =
        Tournament(
            id = entity.id,
            clubId = entity.clubId,
            name = entity.name,
            scheduledAt = entity.scheduledAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            status = EventStatus.valueOf(entity.status),
            scoringType = ScoringType.valueOf(entity.scoringType),
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Tournament): TournamentEntity =
        TournamentEntity(
            id = domain.id,
            clubId = domain.clubId,
            name = domain.name,
            scheduledAt = domain.scheduledAt,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            status = domain.status.name,
            scoringType = domain.scoringType.name,
            syncStatus = domain.syncStatus.name,
        )
}
