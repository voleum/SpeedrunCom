package dev.voleum.speedruncom.adapter.recyclerview

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
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
import dev.voleum.speedruncom.enum.UserNameStyles
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.LeaderboardEmbed
import dev.voleum.speedruncom.ui.screen.LeaderboardItemViewModel
import kotlin.time.ExperimentalTime

class LeaderboardRecyclerViewAdapter :
    RecyclerView.Adapter<LeaderboardRecyclerViewAdapter.LeaderboardViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
    lateinit var trophyAssets: Assets
    var leaderboard: LeaderboardEmbed? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder =
        LeaderboardViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_leaderboard,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = leaderboard?.runs?.size ?: 0

    override fun getItemId(position: Int): Long =
        leaderboard!!.runs[position].hashCode().toLong()

    inner class LeaderboardViewHolder(val binding: HolderLeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onEntryClickListener.onEntryClick(it, layoutPosition) }
        }

        private val image: AppCompatImageView =
            itemView.findViewById(R.id.holder_leaderboard_image)

        private val flag: AppCompatImageView =
            itemView.findViewById(R.id.holder_leaderboard_flag)

        @OptIn(ExperimentalTime::class)
        fun bind(position: Int) {

            val viewModel =
                LeaderboardItemViewModel(
                    leaderboard!!.game.data.ruleset,
                    leaderboard!!.runs[position],
                    leaderboard!!.players.data.find {
                        it.id == leaderboard!!.runs[position].run.players[0].id
                    }
                )
            binding.viewModel = viewModel

            val place = viewModel.placeValue
            if (place in 1..4) {
                loadImage(
                    when (place) {
                        1 -> trophyAssets.trophyFirst
                        2 -> trophyAssets.trophySecond
                        3 -> trophyAssets.trophyThird
                        else -> trophyAssets.trophyForth
                    }
                )
            }
            else clearImage()
            binding.holderLeaderboardConstraint.setBackgroundColor(
                if (position % 2 == 0) SpeedrunCom.instance.resources.getColor(android.R.color.transparent)
                else SpeedrunCom.instance.resources.getColor(R.color.colorPrimaryAlpha25)
            )
            when (viewModel.user?.nameStyle?.style) {
                UserNameStyles.SOLID.value -> binding.holderLeaderboardPlayer.setTextColor(
                    Color.parseColor(viewModel.user.nameStyle.color.light)
                )
                UserNameStyles.GRADIENT.value -> {
                    val tileMode = Shader.TileMode.CLAMP
                    val linearGradient = LinearGradient(
                        0.0F,
                        0.0F,
                        binding.holderLeaderboardPlayer.width.toFloat(),
                        binding.holderLeaderboardPlayer.textSize,
                        Color.parseColor(viewModel.user.nameStyle.colorFrom.light),
                        Color.parseColor(viewModel.user.nameStyle.colorTo.light),
                        tileMode
                    )
                    binding.holderLeaderboardPlayer.paint.shader = linearGradient
                }
            }
            val countryCode = viewModel.user?.location?.country?.code ?: ""
            if (countryCode.isNotEmpty())
                setFlag(countryCode)
        }

        private fun loadImage(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(image)
            }
        }

        private fun clearImage() {
            GlideApp.with(itemView)
                .clear(image)
        }

        private fun setFlag(code: String) {
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
}