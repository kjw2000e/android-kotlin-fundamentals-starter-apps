package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(private val finalScore: Int) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _isRestart = MutableLiveData<Boolean>()
    val isRestart: LiveData<Boolean>
        get() = _isRestart

    init {
        _score.value = finalScore
        _isRestart.value = false
    }

    fun onRestart() {
        _isRestart.value = true
    }

    fun finishRestart() {
        _isRestart.value = false
    }
}