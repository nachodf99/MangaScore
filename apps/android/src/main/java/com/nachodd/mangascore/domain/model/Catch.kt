package com.nachodd.mangascore.domain.model

data class Catch(
    val id: String,
    val participantId: String,
    val clubId: String,
    val weightGrams: Int,
    val caughtAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val registrationId: String? = null,
    val roundId: String? = null,
    val tournamentId: String? = null,
    val species: String? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
