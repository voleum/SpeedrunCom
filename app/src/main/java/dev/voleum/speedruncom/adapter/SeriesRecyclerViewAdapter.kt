package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.HolderSeriesBinding
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.ui.tab.series.SeriesItemViewModel

class SeriesRecyclerViewAdapter :
    RecyclerView.Adapter<SeriesRecyclerViewAdapter.SeriesViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener

    val items = mutableListOf<Series>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder =
        SeriesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_series,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.binding.series =
            SeriesItemViewModel(items[position])
        holder.loadImage((holder.binding.series as SeriesItemViewModel).imageUrl)
    }

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun addItems(items: List<Series>, positionStart: Int, itemCount: Int) {
        this.items.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun replaceItems(items: List<Series>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class SeriesViewHolder(val binding: HolderSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { v ->
                onEntryClickListener.onEntryClick(v, layoutPosition)
            }
        }

        private val image: AppCompatImageView = binding.root.findViewById(R.id.holder_series_image)

        fun loadImage(url: String) =
            GlideApp.with(itemView)
                .load(url)
                .placeholder(R.drawable.ic_baseline_image_200)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
//                .onlyRetrieveFromCache(true)
                .into(image)
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}