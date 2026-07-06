package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.SeasonEntity
import com.nachodd.mangascore.domain.model.EventStatus
import com.nachodd.mangascore.domain.model.ScoringType
import com.nachodd.mangascore.domain.model.Season
import com.nachodd.mangascore.domain.model.SyncStatus

object SeasonMapper {
    fun toDomain(entity: SeasonEntity): Season =
        Season(
            id = entity.id,
            clubId = entity.clubId,
            name = entity.name,
            startsAt = entity.startsAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            endsAt = entity.endsAt,
            status = EventStatus.valueOf(entity.status),
            scoringType = ScoringType.valueOf(entity.scoringType),
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
            discardWorstRounds = entity.discardWorstRounds,
        )

    fun toEntity(domain: Season): SeasonEntity =
        SeasonEntity(
            id = domain.id,
            clubId = domain.clubId,
            name = domain.name,
            startsAt = domain.startsAt,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            endsAt = domain.endsAt,
            status = domain.status.name,
            scoringType = domain.scoringType.name,
            syncStatus = domain.syncStatus.name,
            discardWorstRounds = domain.discardWorstRounds,
        )
}
