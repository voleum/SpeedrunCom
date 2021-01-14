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
import dev.voleum.speedruncom.databinding.HolderRunBinding
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.enum.UserNameStyles
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.RunEmbed
import dev.voleum.speedruncom.ui.nav.home.RunsItemViewModel
import kotlin.time.ExperimentalTime

class RunsRecyclerViewAdapter : RecyclerView.Adapter<RunsRecyclerViewAdapter.RunViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
    val items = mutableListOf<RunEmbed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder =
        RunViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_run,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun addItems(items: List<RunEmbed>, positionStart: Int, itemCount: Int) {
        this.items.addAll(items)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    fun replaceItems(items: List<RunEmbed>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class RunViewHolder(val binding: HolderRunBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener { onEntryClickListener.onEntryClick(it, layoutPosition) }
        }

        private val cover: AppCompatImageView =
            binding.root.findViewById(R.id.holder_run_cover)

        private val flag: AppCompatImageView =
            itemView.findViewById(R.id.holder_run_flag)

        @OptIn(ExperimentalTime::class)
        fun bind(position: Int) {
            val viewModel = RunsItemViewModel(items[position])
            binding.viewModel = viewModel

            loadCover(viewModel.run.game.data.assets.coverMedium)

            val player = viewModel.run.players.data[0]
            if (player.rel == PlayerTypes.USER.value) {
                when (player.nameStyle.style) {
                    UserNameStyles.SOLID.value -> binding.holderRunPlayer.setTextColor(
                        Color.parseColor(player.nameStyle.color.light)
                    )
                    UserNameStyles.GRADIENT.value -> {
                        val tileMode = Shader.TileMode.CLAMP
                        val linearGradient = LinearGradient(
                            0.0F,
                            0.0F,
                            binding.holderRunPlayer.width.toFloat(),
                            binding.holderRunPlayer.textSize,
                            Color.parseColor(player.nameStyle.colorFrom.light),
                            Color.parseColor(player.nameStyle.colorTo.light),
                            tileMode
                        )
                        binding.holderRunPlayer.paint.shader = linearGradient
                    }
                }

                val countryCode = player.location?.country?.code ?: ""
                if (countryCode.isNotEmpty()) setFlag(countryCode)
            }
        }

        private fun loadCover(asset: Asset?) {
            if (asset != null) {
                GlideApp.with(itemView)
                    .load(asset.uri)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .circleCrop()
                    .into(cover)
            }
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