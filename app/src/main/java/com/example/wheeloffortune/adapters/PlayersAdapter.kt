package com.example.wheeloffortune.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.wheeloffortune.Player
import com.example.wheeloffortune.R

class PlayersAdapter(
    private val players: ArrayList<Player>
) :
    RecyclerView.Adapter<PlayersAdapter.PlayersHolder>() {

    inner class PlayersHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nickname: TextView = view.findViewById(R.id.nickname)
        val score: TextView = view.findViewById(R.id.score)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false)
        return PlayersHolder(itemView)
    }

    override fun onBindViewHolder(holder: PlayersHolder, position: Int) {
        val player = players[position]
        holder.nickname.text = player.nickname
        holder.score.text = player.score.toString()
    }

    override fun getItemCount(): Int {
        return players.size
    }
}
