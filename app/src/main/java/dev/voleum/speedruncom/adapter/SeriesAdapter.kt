package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.HolderSeriesBinding
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.ui.games.SeriesItemViewModel

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {

    val items = mutableListOf<Series>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder =
        SeriesViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.holder_series, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.binding.series = SeriesItemViewModel(items[position])
    }

    fun addItems(items: List<Series>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(val binding: HolderSeriesBinding) : RecyclerView.ViewHolder(binding.root)
}