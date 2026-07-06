package com.nachodd.mangascore.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "participants",
    indices = [Index(value = ["clubId"])],
)
data class ParticipantEntity(
    @PrimaryKey val id: String,
    val clubId: String,
    val fullName: String,
    val createdAt: Long,
    val updatedAt: Long,
    val alias: String? = null,
    val licenseNumber: String? = null,
    val syncStatus: String,
)
