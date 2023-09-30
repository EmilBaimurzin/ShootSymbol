package com.shoot.game.domain.repositories

import com.shoot.game.data.data_base.Database
import com.shoot.game.data.data_base.ScoreDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShootGameRepository {
    suspend fun addScore(score: Int) {
        withContext(Dispatchers.Default) {
            val scoreDB = Database.instance.dao().checkScore(score)
            if (scoreDB == null) {
                Database.instance.dao().addScore(ScoreDB(0,  score))
            }
        }
    }
}