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
    val phoneNumber: String? = null,
    val dni: String? = null,
    val nir: String? = null,
    val nira: String? = null,
    val syncStatus: String,
)
