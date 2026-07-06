package com.nachodd.mangascore.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.nachodd.mangascore.data.local.entity.RegistrationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistrationDao {
    @Query("SELECT * FROM registrations WHERE participantId = :participantId ORDER BY registeredAt DESC")
    fun observeByParticipant(participantId: String): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE seasonId = :seasonId ORDER BY registeredAt DESC")
    fun observeBySeason(seasonId: String): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE roundId = :roundId ORDER BY registeredAt DESC")
    fun observeByRound(roundId: String): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE tournamentId = :tournamentId ORDER BY registeredAt DESC")
    fun observeByTournament(tournamentId: String): Flow<List<RegistrationEntity>>

    @Query("SELECT * FROM registrations WHERE id = :id")
    suspend fun getById(id: String): RegistrationEntity?

    @Upsert
    suspend fun upsert(registration: RegistrationEntity)

    @Upsert
    suspend fun upsertAll(registrations: List<RegistrationEntity>)

    @Query("DELETE FROM registrations WHERE id = :id")
    suspend fun deleteById(id: String)
}
