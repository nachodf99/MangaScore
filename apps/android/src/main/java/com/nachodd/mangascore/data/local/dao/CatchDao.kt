package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.CatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatchDao {
    @Query("SELECT * FROM catches WHERE roundId = :roundId ORDER BY caughtAt ASC")
    fun observeByRound(roundId: String): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE tournamentId = :tournamentId ORDER BY caughtAt ASC")
    fun observeByTournament(tournamentId: String): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE syncStatus != 'SYNCED' ORDER BY updatedAt ASC")
    fun observePendingSync(): Flow<List<CatchEntity>>

    @Query("SELECT * FROM catches WHERE id = :id")
    suspend fun getById(id: String): CatchEntity?

    @Upsert
    suspend fun upsert(catchEntity: CatchEntity)

    @Upsert
    suspend fun upsertAll(catches: List<CatchEntity>)

    @Query("DELETE FROM catches WHERE id = :id")
    suspend fun deleteById(id: String)
}
