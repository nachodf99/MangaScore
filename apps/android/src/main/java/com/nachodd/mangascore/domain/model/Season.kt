package com.nachodd.mangascore.domain.model

data class Season(
    val id: String,
    val clubId: String,
    val name: String,
    val startsAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val endsAt: Long? = null,
    val status: EventStatus = EventStatus.SCHEDULED,
    val scoringType: ScoringType = ScoringType.TOTAL_WEIGHT,
    val syncStatus: SyncStatus = SyncStatus.SYNCED,
    val discardWorstRounds: Int = 0,
)
