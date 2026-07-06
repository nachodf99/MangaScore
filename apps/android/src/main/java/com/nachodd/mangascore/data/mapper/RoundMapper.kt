package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.RoundEntity
import com.nachodd.mangascore.domain.model.EventStatus
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.model.ScoringType
import com.nachodd.mangascore.domain.model.SyncStatus

object RoundMapper {
    fun toDomain(entity: RoundEntity): Round =
        Round(
            id = entity.id,
            seasonId = entity.seasonId,
            clubId = entity.clubId,
            name = entity.name,
            scheduledAt = entity.scheduledAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            status = EventStatus.valueOf(entity.status),
            scoringType = ScoringType.valueOf(entity.scoringType),
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Round): RoundEntity =
        RoundEntity(
            id = domain.id,
            seasonId = domain.seasonId,
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
