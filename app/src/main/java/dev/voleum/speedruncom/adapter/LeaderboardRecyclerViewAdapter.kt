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
import dev.voleum.speedruncom.SpeedrunCom
import dev.voleum.speedruncom.databinding.HolderLeaderboardBinding
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.trophyAssetsPlaces
import dev.voleum.speedruncom.ui.screen.LeaderboardItemViewModel

class LeaderboardRecyclerViewAdapter() : RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder>() {

    init {
        //FIXME: doesn't work
        setHasStableIds(true)
    }

    val items = mutableListOf<RunLeaderboard>()
    lateinit var trophyAssets: Assets

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardRecyclerViewAdapter.LeaderboardViewHolder =
        LeaderboardViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_leaderboard,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LeaderboardRecyclerViewAdapter.LeaderboardViewHolder, position: Int) {
        holder.binding.viewModel =
            LeaderboardItemViewModel(items[position])
        val place = (holder.binding.viewModel as LeaderboardItemViewModel).place.toInt()
        if (place <= trophyAssetsPlaces)
            holder.loadImage(when (place) {
                1 -> trophyAssets.trophyFirst
                2 -> trophyAssets.trophySecond
                3 -> trophyAssets.trophyThird
                else -> trophyAssets.trophyForth
            })
        holder.binding.holderLeaderboardConstraint.setBackgroundColor(
            if (position%2 == 0) SpeedrunCom.instance.resources.getColor(android.R.color.transparent)
            else SpeedrunCom.instance.resources.getColor(R.color.colorPrimaryAlpha25)
        )
    }

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun replaceItems(items: List<RunLeaderboard>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class LeaderboardViewHolder(val binding: HolderLeaderboardBinding) : RecyclerView.ViewHolder(binding.root) {

        private val image: AppCompatImageView =
            binding.root.findViewById(R.id.holder_leaderboard_image)

        fun loadImage(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
//                .placeholder(R.drawable.ic_baseline_image_200)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
//                .onlyRetrieveFromCache(true)
                    .into(image)
//                binding.viewModel.imageWidth = asset.width
//                binding.viewModel.imageHeight = asset.height
            }
        }
    }
}