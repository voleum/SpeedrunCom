package dev.voleum.speedruncom.ui.tab.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentTabGamesBinding
import dev.voleum.speedruncom.enum.States
import kotlinx.android.synthetic.main.fragment_tab_games.*

class TabGamesFragment : Fragment() {

    private lateinit var tabGamesViewModel: TabGamesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tabGamesViewModel = ViewModelProvider(this).get(TabGamesViewModel::class.java)
        val binding: FragmentTabGamesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tab_games, null, false)
        binding.viewModel = tabGamesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = binding.gamesRecyclerView
        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.games_columns))

        tabGamesViewModel.adapter.onEntryClickListener =
            object : GamesRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString("game", tabGamesViewModel.data[position].names.international)
                    }
                    findNavController().navigate(R.id.action_game, bundle)
                }
            }

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.gamesSwipeRefreshLayout
        val fab = binding.gamesFab
        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                tabGamesViewModel.state = States.PROGRESS
                Log.d("tag", "onScrolled()")
                tabGamesViewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (tabGamesViewModel.state) {
            States.CREATED -> {
                tabGamesViewModel.setListener { checkData() }
                tabGamesViewModel.load()
            }
            States.PROGRESS -> {
                tabGamesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                tabGamesViewModel.setListener { checkData() }
                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        tabGamesViewModel.state = States.PROGRESS
                        tabGamesViewModel.load()
                    }
                    .show()
//                gamesViewModel.load()
            }
            States.LOADED -> {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}