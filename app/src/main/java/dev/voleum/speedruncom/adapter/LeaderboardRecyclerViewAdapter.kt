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
import dev.voleum.speedruncom.enum.UserNameStyles
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.LeaderboardEmbed
import dev.voleum.speedruncom.ui.screen.LeaderboardItemViewModel

class LeaderboardRecyclerViewAdapter :
    RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
    lateinit var trophyAssets: Assets
    var leaderboard: LeaderboardEmbed? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeaderboardRecyclerViewAdapter.LeaderboardViewHolder =
        LeaderboardViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_leaderboard,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = leaderboard?.runs?.size ?: 0

    override fun onBindViewHolder(
        holder: LeaderboardRecyclerViewAdapter.LeaderboardViewHolder,
        position: Int
    ) {
        val viewModel =
            LeaderboardItemViewModel(
                leaderboard!!.runs[position],
                leaderboard!!.players.data.find {
                    it.id == leaderboard!!.runs[position].run.players[0].id
                }
            )
        holder.binding.viewModel = viewModel

        val place = viewModel.placeValue
        if (place in 1..4) {
            holder.loadImage(
                when (place) {
                    1 -> trophyAssets.trophyFirst
                    2 -> trophyAssets.trophySecond
                    3 -> trophyAssets.trophyThird
                    else -> trophyAssets.trophyForth
                }
            )
        }
        else holder.clearImage()
        holder.binding.holderLeaderboardConstraint.setBackgroundColor(
            if (position % 2 == 0) SpeedrunCom.instance.resources.getColor(android.R.color.transparent)
            else SpeedrunCom.instance.resources.getColor(R.color.colorPrimaryAlpha25)
        )
        when (viewModel.user?.nameStyle?.style) {
            UserNameStyles.SOLID.style -> holder.binding.holderLeaderboardPlayer.setTextColor(
                Color.parseColor(viewModel.user.nameStyle.color.light)
            )
            UserNameStyles.GRADIENT.style -> {
                val tileMode = Shader.TileMode.CLAMP
                val linearGradient = LinearGradient(
                    0.0F,
                    0.0F,
                    holder.binding.holderLeaderboardPlayer.width.toFloat(),
                    holder.binding.holderLeaderboardPlayer.textSize,
                    Color.parseColor(viewModel.user.nameStyle.colorFrom.light),
                    Color.parseColor(viewModel.user.nameStyle.colorTo.light),
                    tileMode
                )
                holder.binding.holderLeaderboardPlayer.paint.shader = linearGradient
            }
        }
        val countryCode = viewModel.user?.location?.country?.code ?: ""
        if (countryCode.isNotEmpty())
            holder.setFlag(countryCode)
    }

    override fun getItemId(position: Int): Long =
        leaderboard!!.runs[position].hashCode().toLong()

    inner class LeaderboardViewHolder(val binding: HolderLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

        fun clearImage() {
            GlideApp.with(itemView)
                .clear(image)
        }

        fun setFlag(code: String) {
            GlideApp.with(itemView)
                .load(
                    SpeedrunCom.instance.resources.getIdentifier(
                        "flag_round_$code",
                        "drawable",
                        SpeedrunCom.instance.packageName
                    )
                )
                .into(flag)
        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}