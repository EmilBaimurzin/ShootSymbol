package com.shoot.game.domain.repositories

import com.shoot.game.data.data_base.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FinishRepository {
    suspend fun getHighestScore(): Int {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.Default).launch {
                delay(20)
                val scoreList = Database.instance.dao().getScores()
                val score = scoreList.maxBy { it.score }.score
                continuation.resume(score)
            }
        }
    }
}