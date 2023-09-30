package com.shoot.game.domain.repositories

import com.shoot.game.data.data_base.Database
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ScoresRepository {
    suspend fun getScores(): List<Int> {
        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.Default).launch {
                val list = Database.instance.dao().getScores()
                val newList = mutableListOf<Int>()
                list.forEach {
                    newList.add(it.score)
                }
                newList.sortBy { it }
                continuation.resume(newList)
            }
        }
    }

    fun clear() {
        CoroutineScope(Dispatchers.Default).launch {
            Database.instance.dao().clear()
        }
    }
}