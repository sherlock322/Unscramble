package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount


    private var wordList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    init {
        getNextWorld()
    }

    private fun getNextWorld () {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord) == currentWord) {
            tempWord.shuffle()
        }

        if (wordList.contains(currentWord)) {
            getNextWorld()
        } else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordList.add(currentWord)
        }
    }

    fun nextWord (): Boolean {
        return if (currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWorld()
            true
        } else false
    }

    private fun increaseScore () {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect (playerWord: String): Boolean {
        return if (playerWord == currentWord) {
            increaseScore()
            true
        } else {
            false
        }
    }

    fun reinitializeData () {
        _score.value = 0
        _currentWordCount.value = 0
        wordList.clear()
        getNextWorld()
    }
}
