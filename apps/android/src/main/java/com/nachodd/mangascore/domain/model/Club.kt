package com.nachodd.mangascore.domain.model

data class Club(
    val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val description: String? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
