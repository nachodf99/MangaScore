package com.nachodd.mangascore.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rounds",
    indices = [
        Index(value = ["seasonId"]),
        Index(value = ["clubId"]),
    ],
)
data class RoundEntity(
    @PrimaryKey val id: String,
    val seasonId: String,
    val clubId: String,
    val name: String,
    val scheduledAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String,
    val scoringType: String,
    val syncStatus: String,
)
