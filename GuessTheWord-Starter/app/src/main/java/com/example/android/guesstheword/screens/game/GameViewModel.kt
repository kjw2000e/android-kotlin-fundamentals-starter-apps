package com.example.android.guesstheword.screens.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private lateinit var wordList: MutableList<String>
//    var word = ""
//    var score = 0

    // mutableLiveData는 Wrapper Class, Wrapper class는 ArrayList, List 같이 primitve class를 감싸는? 클래스
    // ArrayList<String>()   list.get()
    val word = MutableLiveData<String>()
    val score = MutableLiveData<Int>()

//    val list = listOf<String>("sss", "sssss") // MutableLiveData는 value로 접근 가능, but list 형태는 불가능

    init {
        // mutableLiveData 변수 초기화 .value로 접근
        // 값 변경하려면 setValue() 호출 == .value
        word.value = ""
        score.value = 0

        requestList()
        nextWord()
    }

    private fun requestList() {
        wordList = mutableListOf("kotiln", "mvvm", "liveData", "viewModel", "dataBinding")
        wordList.shuffle()
    }

    private fun nextWord() {
        if (!wordList.isEmpty()) {
//            word = wordList.removeAt(0)
            word.value = wordList.removeAt(0)
        }
    }

    fun onCorrect() {
        if (!wordList.isEmpty()) {
//            score++
            score.value = score.value?.plus(1) // safe call 필요 getValue() 함수가 nullable 하므로
            nextWord()
        }
    }

    fun onSkip() {
        if (!wordList.isEmpty()) {
//            score--
            score.value = score.value?.minus(1)
            nextWord()
        }
    }
}