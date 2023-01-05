package com.example.android.guesstheword.screens.game

import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private lateinit var wordList: MutableList<String>
    var word = ""
    var score = 0

    init {
        requestList()
        nextWord()
    }

    private fun requestList() {
        wordList = mutableListOf("kotiln", "mvvm", "liveData", "viewModel", "dataBinding")
        wordList.shuffle()
    }

    private fun nextWord() {
        if (!wordList.isEmpty()) {
            word = wordList.removeAt(0)
        }
    }

    fun onCorrect() {
        if (!wordList.isEmpty()) {
            score++
            nextWord()
        }
    }

    fun onSkip() {
        if (!wordList.isEmpty()) {
            score--
            nextWord()
        }
    }
}