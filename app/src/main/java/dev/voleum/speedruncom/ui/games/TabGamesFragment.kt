package dev.voleum.speedruncom.ui.games

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentTabGamesBinding
import dev.voleum.speedruncom.enum.States

class TabGamesFragment : Fragment() {

    private lateinit var gamesViewModel: GamesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gamesViewModel = ViewModelProvider(this).get(GamesViewModel::class.java)
        val binding: FragmentTabGamesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_games, null, false)
        binding.viewModel = gamesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = binding.gamesRecyclerView
        val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = gamesViewModel.adapter
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.gamesSwipeRefreshLayout
        val fab = binding.gamesFab
        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                gamesViewModel.state = States.PROGRESS
                Log.d("tag", "onScrolled()")
                gamesViewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (gamesViewModel.state) {
            States.CREATED -> {
                gamesViewModel.setListener { checkData() }
                gamesViewModel.load()
            }
            States.PROGRESS -> {
                gamesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                gamesViewModel.setListener { checkData() }
                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(swipeRefreshLayout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        gamesViewModel.state = States.PROGRESS
                        gamesViewModel.load()
                    }
                    .show()
//                gamesViewModel.load()
            }
            States.LOADED -> {
                gamesViewModel.adapter.replaceItems(gamesViewModel.data)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}