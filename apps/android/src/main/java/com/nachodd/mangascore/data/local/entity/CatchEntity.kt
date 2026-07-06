package com.nachodd.mangascore.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "catches",
    indices = [
        Index(value = ["participantId"]),
        Index(value = ["clubId"]),
        Index(value = ["registrationId"]),
        Index(value = ["roundId"]),
        Index(value = ["tournamentId"]),
        Index(value = ["syncStatus"]),
    ],
)
data class CatchEntity(
    @PrimaryKey val id: String,
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
    val syncStatus: String,
)
