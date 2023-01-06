package com.example.android.guesstheword.screens.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private lateinit var wordList: MutableList<String>
//    var word = ""
//    var score = 0

    // mutableLiveData는 Wrapper Class, Wrapper class는 ArrayList, List 같이 primitve class를 감싸는? 클래스
    // ArrayList<String>()   list.get()
    private val _word = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()
    private val _isFinish = MutableLiveData<Boolean>()

    // 캡슐화, kotlin backing properties
    val word: LiveData<String>
        get() = _word

    val score: LiveData<Int>
        get() = _score

    val isFinish: LiveData<Boolean>
        get() = _isFinish

//    val list = listOf<String>("sss", "sssss") // MutableLiveData는 value로 접근 가능, but list 형태는 불가능

    init {
        // mutableLiveData 변수 초기화 .value로 접근
        // 값 변경하려면 setValue() 호출 == .value
        _word.value = ""
        _score.value = 0

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
            _word.value = wordList.removeAt(0)
        } else {
            _isFinish.value = true
        }
    }

    fun onCorrect() {
        if (!wordList.isEmpty()) {
//            score++
            _score.value = _score.value?.plus(1) // safe call 필요 getValue() 함수가 nullable 하므로
        }
        nextWord()
    }

    fun onSkip() {
        if (!wordList.isEmpty()) {
//            score--
            _score.value = _score.value?.minus(1)
        }
        nextWord()
    }

    fun onFinished() {
        _isFinish.value = true
    }

    fun completeFinish() {
        _isFinish.value = false
    }
}