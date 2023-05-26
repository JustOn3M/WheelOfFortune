package com.example.wheeloffortune.game

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wheeloffortune.Player
import com.example.wheeloffortune.adapters.PlayersAdapter
import com.example.wheeloffortune.R
import com.example.wheeloffortune.databinding.DialogAddNewPlayerBinding
import com.example.wheeloffortune.databinding.FragmentGameBinding
import com.example.wheeloffortune.validateForm
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.lang.IllegalArgumentException


class GameFragment : Fragment(R.layout.fragment_game) {

    private lateinit var binding: FragmentGameBinding
    private lateinit var adapter: PlayersAdapter
    private val viewModel: GameViewModel by activityViewModels()
    private var word = Pair("", "")
    private var rand = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGameBinding.bind(requireView())

        readFromFile()
        initFields()
    }

    override fun onStop() {
        super.onStop()
        viewModel.players.clear()
        viewModel.words.clear()
    }

    private fun initFields(){
        with(binding) {
            if (viewModel.players.size == 4) fab.isVisible = false
            if (viewModel.players.size < 2) btnStart.isEnabled = false
            inputLayout.isVisible = false
            btnConfirm.isVisible = false
        }

        adapter = PlayersAdapter(viewModel.players)

        val playersRV = binding.playersRV
        playersRV.layoutManager = GridLayoutManager(context, 2)
        playersRV.adapter = adapter

        binding.fab.setOnClickListener {
            addNewPlayer()
        }

        binding.btnStart.setOnClickListener {
            startGame()
        }

        binding.btnConfirm.setOnClickListener {
            checkLetter()
            binding.letterInput.setText("")
        }
    }

    private fun readFromFile() {
        val file = File(requireContext().filesDir, "words.txt")
        try {
            BufferedReader(FileReader(file)).use { br ->
                br.lines().forEach {
                    viewModel.words.add(Pair(it.split("|").first(), it.split("|").last()))
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startGame() {
        if (viewModel.words.isEmpty()) {
            Toast.makeText(
                this.context,
                resources.getString(R.string.noWordsException),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        with(binding) {
            fab.isVisible = false
            btnStart.isVisible = false
            inputLayout.isVisible = true
            btnConfirm.isVisible = true

        }

        rand = (0 until viewModel.words.size).random()
        word = viewModel.words[rand]
        binding.definition.text = word.second
        viewModel.cypheredWord = "*".repeat(word.first.length)
        binding.word.text = viewModel.cypheredWord
        binding.turn.text =
            resources.getString(R.string.turn) + " " + viewModel.players[viewModel.activePlayerNum].nickname
    }

    private fun addNewPlayer() {
        val dialogBinding = DialogAddNewPlayerBinding.inflate(layoutInflater)

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
        val dialog = dialogBuilder.create()
        dialog.show()

        val nickname = dialogBinding.nickname
        dialogBinding.addButton.setOnClickListener {
            if (validateForm(dialogBinding.nickname)) {
                viewModel.players.add(Player(nickname.text.toString()))
                adapter.notifyDataSetChanged()
                if (viewModel.players.size == 4) binding.fab.isVisible = false
                if (viewModel.players.size >= 2) binding.btnStart.isEnabled = true
                dialog.dismiss()
            }
        }

        dialogBinding.cancelButton.setOnClickListener { dialog.dismiss() }
    }

    private fun checkLetter() {
        val letter = binding.letterInput.text.toString().uppercase()
        if (letter.isNotEmpty()) {
            if (letter in word.first && letter !in viewModel.checkedLetters) {
                var index = word.first.indexOf(letter)
                while (index != -1) {
                    viewModel.cypheredWord =
                        viewModel.cypheredWord.replaceRange(index, index + 1, letter)
                    index = word.first.indexOf(letter, index + 1)
                    viewModel.players[viewModel.activePlayerNum].score += 1
                }
                binding.word.text = viewModel.cypheredWord
                adapter.notifyDataSetChanged()
                if (word.first == viewModel.cypheredWord) {
                    viewModel.wordsGuessed += 1
                    startNewRound()
                }
                return
            }
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.wrongLetter),
                Toast.LENGTH_SHORT
            ).show()
            passTurn()
        }
        else {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.noLetter),
                Toast.LENGTH_SHORT
            ).show()
            passTurn()
        }
    }

    private fun passTurn() {
        viewModel.activePlayerNum =
            if (viewModel.activePlayerNum < viewModel.players.size - 1) viewModel.activePlayerNum + 1
            else 0
        binding.turn.text =
            resources.getString(R.string.turn) + " " + viewModel.players[viewModel.activePlayerNum].nickname
    }

    private fun startNewRound() {

        passTurn()
        viewModel.words.removeAt(rand)
        viewModel.checkedLetters.clear()

        if (viewModel.words.isNotEmpty() && viewModel.wordsGuessed < 3) {
            rand = (0 until viewModel.words.size).random()
            word = viewModel.words[rand]
            binding.definition.text = word.second
            viewModel.cypheredWord = "*".repeat(word.first.length)
            binding.word.text = viewModel.cypheredWord
        } else checkWinner()
    }

    private fun checkWinner() {

        var maxScore = 0
        var winner = ""
        for (player in viewModel.players) {
            if (maxScore < player.score) {
                maxScore = player.score
                winner = player.nickname
            }
        }

        Toast.makeText(
            requireContext(),
            resources.getString(R.string.winner) + " " + winner,
            Toast.LENGTH_LONG
        ).show()

        viewModel.clearAll()
        findNavController().navigate(R.id.action_gameFragment_to_mainMenuFragment)
    }
}
