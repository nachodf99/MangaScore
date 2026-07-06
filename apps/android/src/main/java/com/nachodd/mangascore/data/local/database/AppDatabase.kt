package com.nachodd.mangascore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nachodd.mangascore.data.local.dao.CatchDao
import com.nachodd.mangascore.data.local.dao.ClubDao
import com.nachodd.mangascore.data.local.dao.ParticipantDao
import com.nachodd.mangascore.data.local.dao.RegistrationDao
import com.nachodd.mangascore.data.local.dao.RoundDao
import com.nachodd.mangascore.data.local.dao.SeasonDao
import com.nachodd.mangascore.data.local.dao.TournamentDao
import com.nachodd.mangascore.data.local.entity.CatchEntity
import com.nachodd.mangascore.data.local.entity.ClubEntity
import com.nachodd.mangascore.data.local.entity.ParticipantEntity
import com.nachodd.mangascore.data.local.entity.RegistrationEntity
import com.nachodd.mangascore.data.local.entity.RoundEntity
import com.nachodd.mangascore.data.local.entity.SeasonEntity
import com.nachodd.mangascore.data.local.entity.TournamentEntity

@Database(
    entities = [
        ClubEntity::class,
        SeasonEntity::class,
        RoundEntity::class,
        TournamentEntity::class,
        ParticipantEntity::class,
        RegistrationEntity::class,
        CatchEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clubDao(): ClubDao
    abstract fun seasonDao(): SeasonDao
    abstract fun roundDao(): RoundDao
    abstract fun tournamentDao(): TournamentDao
    abstract fun participantDao(): ParticipantDao
    abstract fun registrationDao(): RegistrationDao
    abstract fun catchDao(): CatchDao
}
