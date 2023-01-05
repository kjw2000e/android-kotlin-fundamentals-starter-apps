package com.example.android.guesstheword.screens.score

import androidx.lifecycle.ViewModel

class ScoreViewModel(private val finalScore: Int) : ViewModel() {
    var score = finalScore

    init {

    }
}