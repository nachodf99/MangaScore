package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.TournamentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TournamentDao {
    @Query("SELECT * FROM tournaments WHERE clubId = :clubId ORDER BY scheduledAt DESC, name ASC")
    fun observeByClub(clubId: String): Flow<List<TournamentEntity>>

    @Query("SELECT * FROM tournaments WHERE id = :id")
    suspend fun getById(id: String): TournamentEntity?

    @Upsert
    suspend fun upsert(tournament: TournamentEntity)

    @Upsert
    suspend fun upsertAll(tournaments: List<TournamentEntity>)

    @Query("DELETE FROM tournaments WHERE id = :id")
    suspend fun deleteById(id: String)
}
