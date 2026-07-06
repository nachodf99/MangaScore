package com.nachodd.mangascore.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "registrations",
    indices = [
        Index(value = ["participantId"]),
        Index(value = ["clubId"]),
        Index(value = ["seasonId"]),
        Index(value = ["roundId"]),
        Index(value = ["tournamentId"]),
    ],
)
data class RegistrationEntity(
    @PrimaryKey val id: String,
    val participantId: String,
    val clubId: String,
    val competitionType: String,
    val registeredAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val seasonId: String? = null,
    val roundId: String? = null,
    val tournamentId: String? = null,
    val syncStatus: String,
)
