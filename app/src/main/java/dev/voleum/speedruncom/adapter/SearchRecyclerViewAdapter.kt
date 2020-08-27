package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.HolderSearchBinding
import dev.voleum.speedruncom.model.SearchResult
import dev.voleum.speedruncom.ui.nav.search.SearchItemViewModel

class SearchRecyclerViewAdapter :
    RecyclerView.Adapter<SearchRecyclerViewAdapter.SearchViewHolder>() {

    init {
        setHasStableIds(true)
    }

    lateinit var onEntryClickListener: OnEntryClickListener
//    lateinit var trophyAssets: Assets
//    var leaderboard: LeaderboardEmbed? = null
    val searchResult = SearchResult(mutableListOf())

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

    override fun getItemCount(): Int = searchResult.games.size

    override fun onBindViewHolder(
        holder: SearchRecyclerViewAdapter.SearchViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemId(position: Int): Long =
        searchResult.games[position].hashCode().toLong()

    inner class SearchViewHolder(val binding: HolderSearchBinding) :
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

        fun bind(position: Int) {

            val viewModel =
                SearchItemViewModel(
//                    leaderboard!!.runs[position],
//                    leaderboard!!.players.data.find {
//                        it.id == leaderboard!!.runs[position].run.players[0].id
//                    }
                )
            binding.viewModel = viewModel

//            val place = viewModel.placeValue
//            if (place in 1..4) {
//                loadImage(
//                    when (place) {
//                        1 -> trophyAssets.trophyFirst
//                        2 -> trophyAssets.trophySecond
//                        3 -> trophyAssets.trophyThird
//                        else -> trophyAssets.trophyForth
//                    }
//                )
//            }
//            else clearImage()
//            binding.holderLeaderboardConstraint.setBackgroundColor(
//                if (position % 2 == 0) SpeedrunCom.instance.resources.getColor(android.R.color.transparent)
//                else SpeedrunCom.instance.resources.getColor(R.color.colorPrimaryAlpha25)
//            )
//            when (viewModel.user?.nameStyle?.style) {
//                UserNameStyles.SOLID.style -> binding.holderLeaderboardPlayer.setTextColor(
//                    Color.parseColor(viewModel.user.nameStyle.color.light)
//                )
//                UserNameStyles.GRADIENT.style -> {
//                    val tileMode = Shader.TileMode.CLAMP
//                    val linearGradient = LinearGradient(
//                        0.0F,
//                        0.0F,
//                        binding.holderLeaderboardPlayer.width.toFloat(),
//                        binding.holderLeaderboardPlayer.textSize,
//                        Color.parseColor(viewModel.user.nameStyle.colorFrom.light),
//                        Color.parseColor(viewModel.user.nameStyle.colorTo.light),
//                        tileMode
//                    )
//                    binding.holderLeaderboardPlayer.paint.shader = linearGradient
//                }
//            }
//            val countryCode = viewModel.user?.location?.country?.code ?: ""
//            if (countryCode.isNotEmpty())
//                setFlag(countryCode)
        }

//        fun loadImage(asset: Asset?) {
//            if (asset != null) {
//                GlideApp.with(itemView)
//                    .load(asset.uri)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                    .centerCrop()
//                    .into(image)
//            }
//        }
//
//        fun clearImage() {
//            GlideApp.with(itemView)
//                .clear(image)
//        }
//
//        fun setFlag(code: String) {
//            GlideApp.with(itemView)
//                .load(
//                    SpeedrunCom.instance.resources.getIdentifier(
//                        "flag_round_$code",
//                        "drawable",
//                        SpeedrunCom.instance.packageName
//                    )
//                )
//                .into(flag)
//        }
    }

    interface OnEntryClickListener {
        fun onEntryClick(view: View?, position: Int)
    }
}