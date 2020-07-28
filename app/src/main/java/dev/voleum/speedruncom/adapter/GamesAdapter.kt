package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.HolderGameBinding
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.games.GamesItemViewModel

class GamesAdapter : RecyclerView.Adapter<GamesAdapter.GameViewHolder>() {

    init {
        setHasStableIds(true)
    }

    val items = mutableListOf<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder =
        GameViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.holder_game, parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.binding.game = GamesItemViewModel(items[position])
        holder.loadImage((holder.binding.game as GamesItemViewModel).imageUrl)
    }

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun addItems(items: List<Game>, positionStart: Int, itemCount: Int) {
        this.items.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun replaceItems(items: List<Game>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class GameViewHolder(val binding: HolderGameBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: AppCompatImageView = binding.root.findViewById(R.id.holder_game_image)
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
}