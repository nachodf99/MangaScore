package com.nachodd.mangascore.domain.model

data class Tournament(
    val id: String,
    val clubId: String,
    val name: String,
    val scheduledAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val status: EventStatus = EventStatus.SCHEDULED,
    val scoringType: ScoringType = ScoringType.TOTAL_WEIGHT,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
)
