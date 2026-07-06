package com.nachodd.mangascore.domain.model

data class Participant(
    val id: String,
    val clubId: String,
    val fullName: String,
    val createdAt: Long,
    val updatedAt: Long,
    val alias: String? = null,
    val licenseNumber: String? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
