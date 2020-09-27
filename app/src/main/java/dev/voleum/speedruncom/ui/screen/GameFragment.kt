package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.STRING_KEY_GAME
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.viewpager.GameCategoriesViewPagerAdapter
import dev.voleum.speedruncom.databinding.FragmentGameBinding
import dev.voleum.speedruncom.enum.RunTypes
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class GameFragment : AbstractFragment<GameViewModel, FragmentGameBinding>() {

    private lateinit var backgroundImageView: AppCompatImageView

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Snackbar
            .make(binding.gameViewPager, R.string.snackbar_unable_to_load, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_action_retry) { load() }
            .show()
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        arguments?.apply {
            viewModel.id = getString(STRING_KEY_GAME, "")
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

        load()

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
            viewModel.categories.filter { it.type == RunTypes.PER_GAME.value },
            viewModel.id,
            viewModel.trophyAssets
        )
        binding.gameViewPager.adapter = adapter
        TabLayoutMediator(binding.gameTabLayout, binding.gameViewPager) { tab, position ->
            tab.text = viewModel.categories[position].name
        }.attach()
    }

    private fun load() {
        scope.launch {
            if (!viewModel.isLoaded) {
                val jobInfo = launch { viewModel.load() }
                jobInfo.join()
            }
            findNavController().currentDestination?.label = viewModel.name
            setBackgroundImage()
            if (binding.gameViewPager.adapter == null)
                createAdapter()
        }
    }
}