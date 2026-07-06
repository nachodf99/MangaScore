package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.RegistrationEntity
import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Registration
import com.nachodd.mangascore.domain.model.SyncStatus

object RegistrationMapper {
    fun toDomain(entity: RegistrationEntity): Registration =
        Registration(
            id = entity.id,
            participantId = entity.participantId,
            clubId = entity.clubId,
            competitionType = CompetitionType.valueOf(entity.competitionType),
            registeredAt = entity.registeredAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            seasonId = entity.seasonId,
            roundId = entity.roundId,
            tournamentId = entity.tournamentId,
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Registration): RegistrationEntity =
        RegistrationEntity(
            id = domain.id,
            participantId = domain.participantId,
            clubId = domain.clubId,
            competitionType = domain.competitionType.name,
            registeredAt = domain.registeredAt,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            seasonId = domain.seasonId,
            roundId = domain.roundId,
            tournamentId = domain.tournamentId,
            syncStatus = domain.syncStatus.name,
        )
}
