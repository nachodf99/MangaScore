package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.ClubEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClubDao {
    @Query("SELECT * FROM clubs ORDER BY name ASC")
    fun observeAll(): Flow<List<ClubEntity>>

    @Query("SELECT * FROM clubs WHERE id = :id")
    suspend fun getById(id: String): ClubEntity?

    @Upsert
    suspend fun upsert(club: ClubEntity)

    @Upsert
    suspend fun upsertAll(clubs: List<ClubEntity>)

    @Query("DELETE FROM clubs WHERE id = :id")
    suspend fun deleteById(id: String)
}
