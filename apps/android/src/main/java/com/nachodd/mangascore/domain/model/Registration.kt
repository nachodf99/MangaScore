package com.nachodd.mangascore.domain.model

data class Registration(
    val id: String,
    val participantId: String,
    val clubId: String,
    val competitionType: CompetitionType,
    val registeredAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val seasonId: String? = null,
    val roundId: String? = null,
    val tournamentId: String? = null,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
