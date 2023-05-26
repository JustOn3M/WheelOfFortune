package com.example.wheeloffortune.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wheeloffortune.R

class WordsAdapter(
    private val words: ArrayList<Pair<String, String>>
) :
    RecyclerView.Adapter<WordsAdapter.WordsHolder>() {

    inner class WordsHolder(view: View) : RecyclerView.ViewHolder(view) {

        val word: TextView = view.findViewById(R.id.word)
        val definition: TextView = view.findViewById(R.id.definition)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordsHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.word_item, parent, false)
        return WordsHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordsHolder, position: Int) {
        val word = words[position]
        holder.word.text = word.first
        holder.definition.text = word.second
    }

    override fun getItemCount(): Int {
        return words.size
    }
}


