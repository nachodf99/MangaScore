package com.nachodd.mangascore.data.mapper

import com.nachodd.mangascore.data.local.entity.ClubEntity
import com.nachodd.mangascore.domain.model.Club
import com.nachodd.mangascore.domain.model.SyncStatus

object ClubMapper {
    fun toDomain(entity: ClubEntity): Club =
        Club(
            id = entity.id,
            name = entity.name,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            description = entity.description,
            syncStatus = SyncStatus.valueOf(entity.syncStatus),
        )

    fun toEntity(domain: Club): ClubEntity =
        ClubEntity(
            id = domain.id,
            name = domain.name,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            description = domain.description,
            syncStatus = domain.syncStatus.name,
        )
}
