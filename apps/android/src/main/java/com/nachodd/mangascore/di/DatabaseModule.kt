package com.nachodd.mangascore.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nachodd.mangascore.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mangascore.db",
        )
            .addMigrations(MIGRATION_1_2)
            .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE participants_new (
                    id TEXT NOT NULL PRIMARY KEY,
                    clubId TEXT NOT NULL,
                    fullName TEXT NOT NULL,
                    createdAt INTEGER NOT NULL,
                    updatedAt INTEGER NOT NULL,
                    alias TEXT,
                    phoneNumber TEXT,
                    dni TEXT,
                    nir TEXT,
                    nira TEXT,
                    syncStatus TEXT NOT NULL
                )
                """.trimIndent(),
            )
            db.execSQL(
                """
                INSERT INTO participants_new (
                    id,
                    clubId,
                    fullName,
                    createdAt,
                    updatedAt,
                    alias,
                    phoneNumber,
                    dni,
                    nir,
                    nira,
                    syncStatus
                )
                SELECT
                    id,
                    clubId,
                    fullName,
                    createdAt,
                    updatedAt,
                    alias,
                    NULL,
                    NULL,
                    licenseNumber,
                    NULL,
                    syncStatus
                FROM participants
                """.trimIndent(),
            )
            db.execSQL("DROP TABLE participants")
            db.execSQL("ALTER TABLE participants_new RENAME TO participants")
            db.execSQL("CREATE INDEX index_participants_clubId ON participants(clubId)")
        }
    }
}
