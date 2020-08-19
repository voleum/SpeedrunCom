package dev.voleum.speedruncom.adapter

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
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
import dev.voleum.speedruncom.SpeedrunCom
import dev.voleum.speedruncom.databinding.HolderLeaderboardBinding
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.enum.UserNameStyles
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.trophyAssetsPlaces
import dev.voleum.speedruncom.ui.screen.LeaderboardItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LeaderboardRecyclerViewAdapter() : RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: LeaderboardRecyclerViewAdapter.OnEntryClickListener

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
        val viewModel =
            LeaderboardItemViewModel(items[position])
        holder.binding.viewModel = viewModel

        CoroutineScope(Dispatchers.Main).launch {

            val jobUser = launch(start = CoroutineStart.LAZY) {
                viewModel.loadUser(viewModel.record.run.players[0].id)
            }

            if (viewModel.record.run.players[0].rel == PlayerTypes.USER.type)
                jobUser.start()

            val place = viewModel.place.toInt()
            if (place <= trophyAssetsPlaces)
                holder.loadImage(
                    when (place) {
                        1 -> trophyAssets.trophyFirst
                        2 -> trophyAssets.trophySecond
                        3 -> trophyAssets.trophyThird
                        else -> trophyAssets.trophyForth
                    }
                )
            holder.binding.holderLeaderboardConstraint.setBackgroundColor(
                if (position % 2 == 0) SpeedrunCom.instance.resources.getColor(android.R.color.transparent)
                else SpeedrunCom.instance.resources.getColor(R.color.colorPrimaryAlpha25)
            )

            if (jobUser.isActive) {
                jobUser.join()
                when (viewModel.user!!.nameStyle.style) {
                    UserNameStyles.SOLID.style -> holder.binding.holderLeaderboardPlayer.setTextColor(
                        Color.parseColor(viewModel.user!!.nameStyle.color.light)
                    )
                    UserNameStyles.GRADIENT.style -> {
                        val tileMode = Shader.TileMode.CLAMP
                        val linearGradient = LinearGradient(
                            0.0F,
                            0.0F,
                            holder.binding.holderLeaderboardPlayer.width.toFloat(),
                            holder.binding.holderLeaderboardPlayer.textSize,
                            Color.parseColor(viewModel.user!!.nameStyle.colorFrom.light),
                            Color.parseColor(viewModel.user!!.nameStyle.colorTo.light),
                            tileMode
                        )
                        holder.binding.holderLeaderboardPlayer.paint.shader = linearGradient
                    }
                }
                val countryCode = viewModel.user!!.location?.country?.code ?: ""
                if (countryCode.isNotEmpty())
                    holder.setFlag(countryCode)
            }
        }
    }

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun replaceItems(items: List<RunLeaderboard>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class LeaderboardViewHolder(val binding: HolderLeaderboardBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener { v ->
                onEntryClickListener.onEntryClick(v, layoutPosition)
            }
        }

        private val image: AppCompatImageView =
            binding.root.findViewById(R.id.holder_leaderboard_image)

        private val flag: AppCompatImageView =
            binding.root.findViewById(R.id.holder_leaderboard_flag)

        fun loadImage(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(image)
            }
        }

        fun setFlag(code: String) {
            GlideApp.with(itemView)
                .load(SpeedrunCom.instance.resources.getIdentifier(
                    "flag_round_$code",
                    "drawable",
                    SpeedrunCom.instance.packageName
                ))
                .into(flag)
        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}