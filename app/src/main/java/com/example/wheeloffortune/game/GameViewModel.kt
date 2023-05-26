package com.example.wheeloffortune.game

import androidx.lifecycle.ViewModel
import com.example.wheeloffortune.Player

class GameViewModel : ViewModel() {

    val players = ArrayList<Player>()

    val words = ArrayList<Pair<String, String>>()

    var cypheredWord = ""

    var activePlayerNum = 0

    var wordsGuessed = 0

    var checkedLetters = ArrayList<String>()

    fun clearAll() {
        players.clear()
        words.clear()
        checkedLetters.clear()
    }

}