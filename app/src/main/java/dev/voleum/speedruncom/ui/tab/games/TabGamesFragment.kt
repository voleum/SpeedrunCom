package dev.voleum.speedruncom.ui.tab.games

import android.content.res.Configuration
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
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.android.synthetic.main.fragment_tab_games.*
import kotlinx.coroutines.*

class TabGamesFragment : AbstractFragment<TabGamesViewModel, FragmentTabGamesBinding>() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TabGamesViewModel::class.java)
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_games,
                null,
                false
            )
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.gamesRecyclerView

        viewModel.adapter.onEntryClickListener =
            object : GamesRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString("game", viewModel.data[position].id)
                    }
                    findNavController().navigate(R.id.action_game, bundle)
                }
            }

        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.games_columns))

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.gamesSwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            scope.launch {
                viewModel.onRefresh()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        val fab = binding.gamesFab
        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                viewModel.state = States.PROGRESS
                Log.d("tag", "onScrolled()")
                viewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        scope.launch {
            viewModel.load()
            games_progress_bar?.visibility = View.GONE
        }

//        checkData()
        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        newConfig.screenLayout = R.layout.fragment_tab_games
        (binding.gamesRecyclerView.layoutManager as GridLayoutManager).spanCount = resources.getInteger(R.integer.games_columns)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    //    private fun checkData() {
////        if (view == null) return
//
//        when (viewModel.state) {
//            States.CREATED -> {
//                viewModel.setListener { checkData() }
//                viewModel.load()
//            }
//            States.PROGRESS -> {
//                viewModel.setListener { checkData() }
//            }
//            States.ERROR -> {
//                viewModel.setListener { checkData() }
//                swipeRefreshLayout.isRefreshing = false
//                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
//                    .setAction("Retry") {
//                        viewModel.state = States.PROGRESS
//                        viewModel.load()
//                    }
//                    .show()
//            }
//            States.LOADED -> {
//                swipeRefreshLayout.isRefreshing = false
//            }
//        }
//    }
}