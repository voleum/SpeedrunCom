package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.model.Game

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    val items = mutableListOf<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.holder_game, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val item = getItem(position)
        holder.textId.text = item.id
        holder.textAbbreviation.text = item.abbreviation
        holder.textWeblink.text = item.weblink
        holder.textReleased.text = item.released.toString()
        holder.textRomhack.text = item.romhack.toString()
        holder.textCreated.text = item.created
    }

    fun addItems(items: List<Game>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Game = items[position]

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textId = view.findViewById<TextView>(R.id.holder_game_id)
        var textAbbreviation = view.findViewById<TextView>(R.id.holder_game_abbreviation)
        var textWeblink = view.findViewById<TextView>(R.id.holder_game_weblink)
        var textReleased = view.findViewById<TextView>(R.id.holder_game_released)
        var textRomhack = view.findViewById<TextView>(R.id.holder_game_romhack)
        var textCreated = view.findViewById<TextView>(R.id.holder_game_created)
    }
}