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
import dev.voleum.speedruncom.databinding.HolderSearchGameBinding
import dev.voleum.speedruncom.databinding.HolderSearchSeriesBinding
import dev.voleum.speedruncom.model.*
import dev.voleum.speedruncom.ui.nav.search.SearchItemGameViewModel
import dev.voleum.speedruncom.ui.nav.search.SearchItemSeriesViewModel

class SearchRecyclerViewAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
    val searchResult = SearchResult()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_SERIES ->
                SearchSeriesViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.holder_search_series,
                        parent,
                        false
                    )
                )
            else ->
                SearchGameViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.holder_search_game,
                        parent,
                        false
                    )
                )
        }

    override fun getItemCount(): Int = searchResult.size

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (searchResult.getType(position)) {
            TYPE_SERIES -> (holder as SearchSeriesViewHolder).bind(position)
            TYPE_GAME -> (holder as SearchGameViewHolder).bind(position)
        }
    }

    override fun getItemId(position: Int): Long =
        searchResult[position].hashCode().toLong()

    override fun getItemViewType(position: Int): Int {
        return searchResult.getType(position)
    }

    fun clearResult() {
        searchResult.series.clear()
        searchResult.games.clear()
        notifyDataSetChanged()
    }

    fun addSeries(items: List<Series>) {
        this.searchResult.series.addAll(items)
        notifyDataSetChanged()
    }

    fun addGames(items: List<Game>) {
        this.searchResult.games.addAll(items)
        notifyDataSetChanged()
    }

    inner class SearchSeriesViewHolder(val binding: HolderSearchSeriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v ->
                onEntryClickListener.onEntryClick(v, layoutPosition)
            }
        }

        private val cover: AppCompatImageView =
            binding.root.findViewById(R.id.holder_search_cover)

        fun bind(position: Int) {

            val viewModel =
                SearchItemSeriesViewModel(searchResult[position] as Series)
            binding.viewModel = viewModel

            loadCover(viewModel.imageAsset)
        }

        private fun loadCover(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(cover)
            }
        }

    }

    inner class SearchGameViewHolder(val binding: HolderSearchGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { v ->
                onEntryClickListener.onEntryClick(v, layoutPosition)
            }
        }

        private val cover: AppCompatImageView =
            binding.root.findViewById(R.id.holder_search_cover)

        fun bind(position: Int) {

            val viewModel =
                SearchItemGameViewModel(searchResult[position] as Game)
            binding.viewModel = viewModel

            loadCover(viewModel.imageAsset)
        }

        private fun loadCover(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(cover)
            }
        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}