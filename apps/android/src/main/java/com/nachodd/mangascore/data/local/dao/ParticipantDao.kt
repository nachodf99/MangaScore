package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants WHERE clubId = :clubId ORDER BY fullName ASC")
    fun observeByClub(clubId: String): Flow<List<ParticipantEntity>>

    @Query("SELECT * FROM participants WHERE id = :id")
    suspend fun getById(id: String): ParticipantEntity?

    @Upsert
    suspend fun upsert(participant: ParticipantEntity)

    @Upsert
    suspend fun upsertAll(participants: List<ParticipantEntity>)

    @Query("DELETE FROM participants WHERE id = :id")
    suspend fun deleteById(id: String)
}
