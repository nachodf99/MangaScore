package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.SeasonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SeasonDao {
    @Query("SELECT * FROM seasons WHERE clubId = :clubId ORDER BY startsAt DESC, name ASC")
    fun observeByClub(clubId: String): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM seasons WHERE id = :id")
    suspend fun getById(id: String): SeasonEntity?

    @Upsert
    suspend fun upsert(season: SeasonEntity)

    @Upsert
    suspend fun upsertAll(seasons: List<SeasonEntity>)

    @Query("DELETE FROM seasons WHERE id = :id")
    suspend fun deleteById(id: String)
}
