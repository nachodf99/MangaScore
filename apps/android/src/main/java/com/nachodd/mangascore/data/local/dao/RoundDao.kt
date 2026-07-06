package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.RoundEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoundDao {
    @Query("SELECT * FROM rounds WHERE seasonId = :seasonId ORDER BY scheduledAt ASC, name ASC")
    fun observeBySeason(seasonId: String): Flow<List<RoundEntity>>

    @Query("SELECT * FROM rounds WHERE id = :id")
    suspend fun getById(id: String): RoundEntity?

    @Upsert
    suspend fun upsert(round: RoundEntity)

    @Upsert
    suspend fun upsertAll(rounds: List<RoundEntity>)

    @Query("DELETE FROM rounds WHERE id = :id")
    suspend fun deleteById(id: String)
}
