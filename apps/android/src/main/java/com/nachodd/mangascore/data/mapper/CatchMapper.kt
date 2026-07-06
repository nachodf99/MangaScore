package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.CatchEntity
import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.SyncStatus

object CatchMapper {
    fun toDomain(entity: CatchEntity): Catch =
        Catch(
            id = entity.id,
            participantId = entity.participantId,
            clubId = entity.clubId,
            weightGrams = entity.weightGrams,
            caughtAt = entity.caughtAt,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            registrationId = entity.registrationId,
            roundId = entity.roundId,
            tournamentId = entity.tournamentId,
            species = entity.species,
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Catch): CatchEntity =
        CatchEntity(
            id = domain.id,
            participantId = domain.participantId,
            clubId = domain.clubId,
            weightGrams = domain.weightGrams,
            caughtAt = domain.caughtAt,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            registrationId = domain.registrationId,
            roundId = domain.roundId,
            tournamentId = domain.tournamentId,
            species = domain.species,
            syncStatus = domain.syncStatus.name,
        )
}
