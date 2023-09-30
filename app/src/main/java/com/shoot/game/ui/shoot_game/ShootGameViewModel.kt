package com.shoot.game.ui.shoot_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoot.game.core.library.XYIMpl
import com.shoot.game.core.library.random
import com.shoot.game.domain.repositories.ShootGameRepository
import com.shoot.game.domain.Symbol
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShootGameViewModel : ViewModel() {
    private var gameScope = CoroutineScope(Dispatchers.Default)
    private val repository = ShootGameRepository()
    private val _aimY = MutableLiveData(0f)
    val aimY: LiveData<Float> = _aimY

    private val _sliderXY = MutableLiveData(XYIMpl(0f, 0f))
    val sliderXY: LiveData<XYIMpl> = _sliderXY

    private val _symbols = MutableLiveData<List<Symbol>>(emptyList())
    val symbols: LiveData<List<Symbol>> = _symbols

    private val _lives = MutableLiveData(3)
    val lives: LiveData<Int> = _lives

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    var isMovingUp = false
    var gameState = true

    var distance = 15
    var delay = 3111L

    fun setSliderXY(x: Float, y: Float, limitStart: Float, limitEnd: Float) {
        if (x < limitStart) {
            _sliderXY.postValue(XYIMpl(limitStart, y))
            return
        }
        if (x > limitEnd) {
            _sliderXY.postValue(XYIMpl(limitEnd, y))
            return
        }
        _sliderXY.postValue(XYIMpl(x, y))
    }

    fun addScore() {
        viewModelScope.launch {
            repository.addScore(_score.value!!)
        }
    }

    fun start(
        bottomLimit: Int,
        topLimit: Int,
        minY: Int,
        maxY: Int,
        minX: Int,
        maxX: Int,
        symbolWidth: Int,
        symbolHeight: Int
    ) {
        gameScope = CoroutineScope(Dispatchers.Default)
        moveAim(bottomLimit, topLimit)
        generateSymbols(minY, maxY, minX, maxX, symbolWidth, symbolHeight)
        startTime()
        speedUp()
    }

    fun stop() {
        gameScope.cancel()
    }

    private fun generateSymbols(
        minY: Int,
        maxY: Int,
        minX: Int,
        maxX: Int,
        symbolWidth: Int,
        symbolHeight: Int
    ) {
        gameScope.launch {
            while (true) {
                delay(delay)
                val currentList = _symbols.value!!.toMutableList()
                val x = (minX..(maxX - symbolWidth)).random()
                val y = (minY..(maxY - symbolHeight)).random()
                currentList.add(Symbol(x.toFloat(), y.toFloat(), 1 random 5, 15))
                _symbols.postValue(currentList)
            }
        }
    }

    private fun speedUp() {
        gameScope.launch {
            while (true) {
                delay(4000)
                distance += 1
                if (delay >= 1000) {
                    delay -= 100
                }
            }
        }
    }

    private fun startTime() {
        gameScope.launch {
            while (true) {
                delay(1000)
                val currentList = _symbols.value!!.toMutableList()
                currentList.map {
                    it.time = it.time - 1
                }
                currentList.removeAll { it.time <= 0 }
                _symbols.postValue(currentList)
            }
        }
    }

    private fun moveAim(bottomLimit: Int, topLimit: Int) {
        gameScope.launch {
            while (true) {
                delay(16)
                if (_aimY.value!! >= bottomLimit) {
                    isMovingUp = true
                }

                if (_aimY.value!! <= topLimit) {
                    isMovingUp = false
                }

                _aimY.postValue(_aimY.value!! + if (isMovingUp) -distance else distance)
            }
        }
    }

    fun shoot(scopeX: Int, scopeY: Int, scopeSize: Int, padding: Int, symbolSize: Int) {
        viewModelScope.launch {
            val aimX = (scopeX + padding)..(scopeX + scopeSize - padding)
            val aimY = (scopeY + padding)..(scopeY + scopeSize - padding)

            val currentList = _symbols.value!!
            val newList = mutableListOf<Symbol>()

            currentList.forEach { symbol ->
                val symbolX = symbol.x.toInt()..(symbol.x.toInt() + symbolSize)
                val symbolY = symbol.y.toInt()..(symbol.y.toInt() + symbolSize)

                if (aimX.any { it in symbolX } && aimY.any { it in symbolY }) {
                    _score.postValue(_score.value!! + 50)
                } else {
                    newList.add(symbol)
                }
            }

            if (newList.size == currentList.size) {
                _lives.postValue(_lives.value!! - 1)
            }
            _symbols.postValue(newList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}