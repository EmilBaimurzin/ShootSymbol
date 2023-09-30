package com.shoot.game.ui.scores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoot.game.domain.repositories.ScoresRepository
import kotlinx.coroutines.launch

class ScoresViewModel: ViewModel() {
    private val repository = ScoresRepository()
    private val _list = MutableLiveData<List<Int>>(emptyList())
    val list: LiveData<List<Int>> = _list

    init {
        viewModelScope.launch {
            _list.postValue(repository.getScores().reversed())
        }
    }

    fun refresh() {
        _list.postValue(emptyList())
        repository.clear()
    }
}