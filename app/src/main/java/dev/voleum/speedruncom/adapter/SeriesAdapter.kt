package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.voleum.speedruncom.GlideApp
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
        holder.loadImage((holder.binding.series as SeriesItemViewModel).imageUrl)
    }

    fun addItems(items: List<Series>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(val binding: HolderSeriesBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: AppCompatImageView = binding.root.findViewById(R.id.holder_series_image)
        fun loadImage(url: String) =
            GlideApp.with(itemView)
                .load(url)
                .placeholder(R.drawable.ic_baseline_image_24)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(image)
    }
}