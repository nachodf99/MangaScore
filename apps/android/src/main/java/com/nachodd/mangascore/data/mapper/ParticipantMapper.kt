package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.ParticipantEntity
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.SyncStatus

object ParticipantMapper {
    fun toDomain(entity: ParticipantEntity): Participant =
        Participant(
            id = entity.id,
            clubId = entity.clubId,
            fullName = entity.fullName,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            alias = entity.alias,
            licenseNumber = entity.licenseNumber,
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Participant): ParticipantEntity =
        ParticipantEntity(
            id = domain.id,
            clubId = domain.clubId,
            fullName = domain.fullName,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            alias = domain.alias,
            licenseNumber = domain.licenseNumber,
            syncStatus = domain.syncStatus.name,
        )
}
