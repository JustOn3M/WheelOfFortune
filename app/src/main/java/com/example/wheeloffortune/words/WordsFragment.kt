package com.example.wheeloffortune.words

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wheeloffortune.R
import com.example.wheeloffortune.adapters.PlayersAdapter
import com.example.wheeloffortune.adapters.WordsAdapter
import com.example.wheeloffortune.databinding.DialogAddNewWordBinding
import com.example.wheeloffortune.databinding.FragmentWordsBinding
import com.example.wheeloffortune.validateForm
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class WordsFragment : Fragment(R.layout.fragment_words) {

    private lateinit var binding: FragmentWordsBinding
    private lateinit var adapter: WordsAdapter
    private val viewModel: WordsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWordsBinding.bind(requireView())

        initFields()
    }

    override fun onStop() {
        super.onStop()
        viewModel.words.clear()
    }

    private fun initFields(){
        adapter = WordsAdapter(viewModel.words)

        val wordsRV = binding.wordsRV
        wordsRV.layoutManager = LinearLayoutManager(context)
        wordsRV.adapter = adapter

        readFromFile()

        binding.btnAddNew.setOnClickListener { showAddNewDialog() }
        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(resources.getString(R.string.words_delete_alert))
                setMessage(resources.getString(R.string.alert_check))

                setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                    clearFile()
                    viewModel.words.clear()
                    adapter.notifyDataSetChanged()
                }

                setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
                setCancelable(true)
            }.create().show()
        }
    }

    private fun showAddNewDialog() {
        val dialogBinding = DialogAddNewWordBinding.inflate(layoutInflater)

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
        val dialog = dialogBuilder.create()
        dialog.show()

        val wordTV = dialogBinding.word
        val definition = dialogBinding.definition
        dialogBinding.saveButton.setOnClickListener {
            if (validateForm(dialogBinding.word) && validateForm(dialogBinding.definition)) {
                viewModel.words.add(
                    Pair(
                        wordTV.text.toString().uppercase(),
                        definition.text.toString()
                    )
                )
                val file = File(requireContext().filesDir, "words.txt")
                file.appendText(
                    wordTV.text.toString().uppercase() + "|" + definition.text.toString() + "\n"
                )
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialogBinding.cancelButton.setOnClickListener { dialog.dismiss() }
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

        adapter.notifyDataSetChanged()
    }

    private fun clearFile() {
        val file = File(requireContext().filesDir, "words.txt")
        file.writeText("")
    }
}
