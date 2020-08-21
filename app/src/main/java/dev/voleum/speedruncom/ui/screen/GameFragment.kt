package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.tabs.TabLayoutMediator
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GameCategoriesViewPagerAdapter
import dev.voleum.speedruncom.databinding.FragmentGameBinding
import dev.voleum.speedruncom.enum.RunTypes
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class GameFragment : AbstractFragment<GameViewModel, FragmentGameBinding>() {

    private lateinit var backgroundImageView: AppCompatImageView
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        arguments?.apply {
            viewModel.id = getString("game", "")
        }
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_game,
                container,
                false
            )
        binding.viewModel = viewModel
        backgroundImageView = binding.gameBackground

        scope.launch {
            if (!viewModel.isLoaded) {
                val jobInfo = launch { viewModel.load() }
                jobInfo.join()
            }
            setBackgroundImage()
            if (binding.gameViewPager.adapter == null)
                createAdapter()
        }

        return binding.root
    }

    private fun setBackgroundImage() {
        GlideApp.with(this)
                    .load(viewModel.backgroundUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(backgroundImageView)
    }

    private fun createAdapter() {
        val adapter = GameCategoriesViewPagerAdapter(
            this,
            viewModel.categories.filter { it.type == RunTypes.PER_GAME.type },
            viewModel.id,
            viewModel.trophyAssets
        )
        binding.gameViewPager.adapter = adapter
        TabLayoutMediator(binding.gameTabLayout, binding.gameViewPager) { tab, position ->
            tab.text = viewModel.categories[position].name
        }.attach()
    }
}