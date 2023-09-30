package com.shoot.game.data.data_base

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoreDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): ScoresDao

    companion object {
        const val DATABASE_NAME = "app_database"
    }
}