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
import dev.voleum.speedruncom.databinding.HolderSearchBinding
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.nav.search.SearchItemViewModel

class SearchRecyclerViewAdapter :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
    val searchResult = mutableListOf<Any>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchRecyclerViewAdapter.SearchViewHolder =
        SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_search,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = searchResult.size

    override fun onBindViewHolder(
        holder: SearchRecyclerViewAdapter.SearchViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long =
        searchResult[position].hashCode().toLong()

    fun replaceItems(items: List<Any>) {
        this.searchResult.clear()
        this.searchResult.addAll(items)
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(val binding: HolderSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { v ->
                onEntryClickListener.onEntryClick(v, layoutPosition)
            }
        }

        private val image: AppCompatImageView =
            binding.root.findViewById(R.id.holder_search_image)

        fun bind(position: Int) {

            val viewModel =
                SearchItemViewModel(searchResult[position] as Game)
            binding.viewModel = viewModel

            loadImage(viewModel.imageAsset)
        }

        private fun loadImage(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(image)
            }
        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}