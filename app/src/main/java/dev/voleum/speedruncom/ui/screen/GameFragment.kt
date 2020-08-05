package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dev.voleum.speedruncom.GlideApp
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GameCategoriesViewPagerAdapter
import dev.voleum.speedruncom.databinding.FragmentGameBinding
import dev.voleum.speedruncom.enum.States
import kotlinx.android.synthetic.main.fragment_game.*
import kotlinx.android.synthetic.main.fragment_tab_games.*

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    //    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var backgroundImageView: AppCompatImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        arguments?.apply {
            viewModel.id = getString("game", "")
        }
        val binding: FragmentGameBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_game,
                container,
                false
            )
        binding.viewModel = viewModel
//        swipeRefreshLayout = binding.gameSwipeRefreshLayout
        backgroundImageView = binding.gameBackground
        checkData()
//        checkCategories()
        return binding.root
    }

    private fun checkData() {
//        if (view == null) return

        when (viewModel.stateInfo) {
            States.CREATED -> {
                viewModel.setInfoListener { checkData() }
                viewModel.loadInfo()
            }
            States.PROGRESS -> {
                viewModel.setInfoListener { checkData() }
            }
            States.ERROR -> {
                viewModel.setInfoListener { checkData() }
//                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        viewModel.stateInfo = States.PROGRESS
                        viewModel.loadInfo()
                    }
                    .show()
//                gamesViewModel.load()
            }
            States.LOADED -> {
                GlideApp.with(this)
                    .load(viewModel.backgroundUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .centerCrop()
                    .into(backgroundImageView)
//                swipeRefreshLayout.isRefreshing = false
                checkCategories()
            }
        }
    }

    private fun checkCategories() {
//        if (view == null) return

        when (viewModel.stateCategories) {
            States.CREATED -> {
                viewModel.setCategoriesListener { checkCategories() }
                viewModel.loadCategories()
            }
            States.PROGRESS -> {
                viewModel.setCategoriesListener { checkCategories() }
            }
            States.ERROR -> {
                viewModel.setCategoriesListener { checkCategories() }
//                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        viewModel.stateCategories = States.PROGRESS
                        viewModel.loadCategories()
                    }
                    .show()
//                viewModel.load()
            }
            States.LOADED -> {
                //FIXME: FIX. THIS. FREAKING. SHIT.
                val adapter = GameCategoriesViewPagerAdapter(
                    this,
                    viewModel.categories,
                    viewModel.id,
                    viewModel.trophyAssets
                )
                game_view_pager.adapter = adapter
                TabLayoutMediator(game_tab_layout, game_view_pager) { tab, position ->
                    tab.text = viewModel.categories[position].name
                }.attach()
            }
        }
    }
}