package com.nachodd.mangascore.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "seasons",
    indices = [Index(value = ["clubId"])],
)
data class SeasonEntity(
    @PrimaryKey val id: String,
    val clubId: String,
    val name: String,
    val startsAt: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val endsAt: Long? = null,
    val status: String,
    val scoringType: String,
    val syncStatus: String,
    val discardWorstRounds: Int = 0,
)
