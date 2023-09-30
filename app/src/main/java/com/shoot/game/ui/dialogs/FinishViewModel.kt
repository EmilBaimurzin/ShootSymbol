package com.shoot.game.ui.dialogs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoot.game.domain.repositories.FinishRepository
import kotlinx.coroutines.launch

class FinishViewModel: ViewModel() {
    private val repository = FinishRepository()

    private val _bestScore = MutableLiveData(0)
    val bestScore: LiveData<Int> = _bestScore

    init {
        viewModelScope.launch {
            _bestScore.postValue(repository.getHighestScore())
        }
    }
}