package com.shoot.game.data.data_base

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScoresDao {
    @Query("SELECT * FROM ScoreDB")
    fun getScores(): List<ScoreDB>

    @Insert
    fun addScore(score: ScoreDB)

    @Query("SELECT * FROM ScoreDB WHERE score = :score")
    fun checkScore(score: Int): ScoreDB?

    @Query("DELETE FROM ScoreDB")
    fun clear()
}