package com.nachodd.mangascore.domain.model

data class Participant(
    val id: String,
    val clubId: String,
    val fullName: String,
    val createdAt: Long,
    val updatedAt: Long,
    val alias: String? = null,
    val phoneNumber: String? = null,
    val dni: String? = null,
    val nir: String? = null,
    val nira: String? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
