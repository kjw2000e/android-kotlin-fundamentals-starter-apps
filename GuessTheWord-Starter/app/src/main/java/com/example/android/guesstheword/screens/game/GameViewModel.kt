package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    companion object {
        val COUNTDOWN_TIME = 60000L
        val INTERVAL_TIME = 1000L
        val FINISH_TIME = 0L
    }

    private lateinit var wordList: MutableList<String>
//    var word = ""
//    var score = 0

    // mutableLiveData는 Wrapper Class, Wrapper class는 ArrayList, List 같이 primitve class를 감싸는? 클래스
    // ArrayList<String>()   list.get()
    private val _word = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()
    private val _isFinish = MutableLiveData<Boolean>()
    private val _count = MutableLiveData<Long>()

    private val timer: CountDownTimer

    // 캡슐화, kotlin backing properties
    val word: LiveData<String>
        get() = _word

    val score: LiveData<Int>
        get() = _score

    val isFinish: LiveData<Boolean>
        get() = _isFinish

    val count: LiveData<Long>
        get() = _count

    // Transformation string format
    // Transformation 는 메인 쓰레드에서 돌아감.
    val countString = Transformations.map(count) {
        time -> DateUtils.formatElapsedTime(time)
    }

    val scoreString = Transformations.map(score) {
        score -> "Current score : $score"
    }

    val hintString = Transformations.map(word) {
        word ->
            val randomPos = (1..(word.length)).random()
            "Current word has ${word.length} letters\n" +
                    "The letter at position $randomPos is ${word.get(randomPos - 1).toUpperCase()}"
    }

//    val list = listOf<String>("sss", "sssss") // MutableLiveData는 value로 접근 가능, but list 형태는 불가능

    init {
        timer = object : CountDownTimer(COUNTDOWN_TIME, INTERVAL_TIME) {
            override fun onTick(millisUntilFinished: Long) {
                _count.value = millisUntilFinished / INTERVAL_TIME
            }

            override fun onFinish() {
                _count.value = FINISH_TIME
                onFinished()            }
        }
        timer.start()

        // mutableLiveData 변수 초기화 .value로 접근
        // 값 변경하려면 setValue() 호출 == .value
        _word.value = ""
        _score.value = 0

        resetList()
        nextWord()
    }

    private fun resetList() {
        wordList = mutableListOf("kotiln", "mvvm", "liveData", "viewModel", "dataBinding")
        wordList.shuffle()
    }

    private fun nextWord() {
        if (!wordList.isEmpty()) {
//            word = wordList.removeAt(0)
            _word.value = wordList.removeAt(0)
        } else {
            resetList() // 시간 초과시 끝내는 걸로
//            _isFinish.value = true
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