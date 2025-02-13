/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.GameFragmentBinding
import kotlinx.android.synthetic.main.game_fragment.*

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    // The current word
//    private var word = ""
//
//    // The current score
//    private var score = 0
//
//    // The list of words - the front of the list is the next word to guess
//    private lateinit var wordList: MutableList<String>

    //필요한 data는 모두 viewModel에서 다룬다.
    private lateinit var binding: GameFragmentBinding
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.game_fragment,
                container,
                false
        )

        // 바인딩 생성 후 뷰모델 생성
        // 뷰 모델 생성 시 ViewModelProvider() 로 생성해야 뷰 모델이 재생성 되지 않는다.
        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // viewmodel을 데이터 바인딩
        binding.gameViewModel = gameViewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        gameViewModel.word.observe(
//            viewLifecycleOwner,
//            Observer { word -> binding.wordText.text = word })
//        gameViewModel.score.observe(
//            viewLifecycleOwner,
//            Observer { score -> binding.scoreText.text = score.toString() })

        gameViewModel.isFinish.observe(
            viewLifecycleOwner,
            Observer { isFinish ->
                if (isFinish) {
                    // 인텐트 action 보내듯이 값 보낼 때 셋팅 해주면된다.
                    val action = GameFragmentDirections.actionGameToScore()
//            action.score = gameViewModel.score
                    action.score = gameViewModel.score.value ?: 0
                    // gameViewModel.score.value이 null 이면 0을 넣는다. ?:
                    NavHostFragment.findNavController(this).navigate(action)
                    gameViewModel.completeFinish()
                }
            }
        )

//        gameViewModel.count.observe(
//            viewLifecycleOwner,
//            Observer {
//                count -> binding.timerText.text = count.toString()
//            }
//        )

//
//        resetList()
//        nextWord()

        binding.correctButton.setOnClickListener {
//            onCorrect()
//            gameViewModel.onCorrect()
//            updateScoreText()
//            updateWordText()
        }
        binding.skipButton.setOnClickListener {
            gameViewModel.onSkip()
//            updateScoreText()
//            updateWordText()
        }

        binding.endGameButton.setOnClickListener {
            gameViewModel.onFinished()
        }

        updateScoreText()
        updateWordText()
        return binding.root

    }

    /**
     * Resets the list of words and randomizes the order
     */
//    private fun resetList() {
//        wordList = mutableListOf(
//                "queen",
//                "hospital",
//                "basketball",
//                "cat",
//                "change",
//                "snail",
//                "soup",
//                "calendar",
//                "sad",
//                "desk",
//                "guitar",
//                "home",
//                "railway",
//                "zebra",
//                "jelly",
//                "car",
//                "crow",
//                "trade",
//                "bag",
//                "roll",
//                "bubble"
//        )
//        wordList.shuffle()
//    }

    /** Methods for buttons presses **/

    private fun onSkip() {
//        score--
//        nextWord()
    }

    private fun onCorrect() {
//        score++
//        nextWord()
    }

    /**
     * Moves to the next word in the list
     */
//    private fun nextWord() {
//        if (!wordList.isEmpty()) {
//            //Select and remove a word from the list
//            word = wordList.removeAt(0)
//        }
//        updateWordText()
//        updateScoreText()
//    }


    /** Methods for updating the UI **/

    private fun updateWordText() {
//        binding.wordText.text = gameViewModel.word
        binding.wordText.text = gameViewModel.word.value // mutableLiveData value로 접근
    }

    private fun updateScoreText() {
        binding.scoreText.text = gameViewModel.score.value.toString()
    }
}
