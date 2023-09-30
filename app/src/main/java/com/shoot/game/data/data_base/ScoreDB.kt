package com.shoot.game.data.data_base

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreDB (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val score: Int
)