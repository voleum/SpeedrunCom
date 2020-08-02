package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentGameBinding
import dev.voleum.speedruncom.enum.States
import kotlinx.android.synthetic.main.fragment_tab_games.*

class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var backgroundImageView: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameViewModel =
            ViewModelProvider(this).get(GameViewModel::class.java)
        arguments?.apply {
            gameViewModel.id = getString("game", "")
        }
        val binding: FragmentGameBinding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_game,
                container,
                false)
        binding.viewModel = gameViewModel
        swipeRefreshLayout = binding.gameSwipeRefreshLayout
        backgroundImageView = binding.gameBackground
        checkData()
        return binding.root
    }

    private fun checkData() {
//        if (view == null) return

        when (gameViewModel.state) {
            States.CREATED -> {
                gameViewModel.setListener { checkData() }
                gameViewModel.load()
            }
            States.PROGRESS -> {
                gameViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                gameViewModel.setListener { checkData() }
                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        gameViewModel.state = States.PROGRESS
                        gameViewModel.load()
                    }
                    .show()
//                gamesViewModel.load()
            }
            States.LOADED -> {
                GlideApp.with(this)
                    .load(gameViewModel.backgroundUrl)
//                    .placeholder(R.drawable.ic_baseline_image_200)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
//                .onlyRetrieveFromCache(true)
                    .into(backgroundImageView)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}