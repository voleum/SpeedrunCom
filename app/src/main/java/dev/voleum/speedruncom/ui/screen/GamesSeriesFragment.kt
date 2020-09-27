package dev.voleum.speedruncom.ui.screen

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.*
import dev.voleum.speedruncom.adapter.recyclerview.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentGamesSeriesBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class GamesSeriesFragment : AbstractFragment<GamesSeriesViewModel, FragmentGamesSeriesBinding>() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Snackbar
            .make(binding.gamesRecyclerView, R.string.snackbar_unable_to_load, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_action_retry) { load() }
            .show()
        swipeRefreshLayout.isRefreshing = false
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GamesSeriesViewModel::class.java)
        arguments?.apply {
            viewModel.seriesId = getString(STRING_KEY_SERIES, "")
        }
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_games_series,
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
                        putString(STRING_KEY_GAME, viewModel.data[position].id)
                    }
                    findNavController().navigate(R.id.action_game, bundle)
                }
            }

        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.games_columns))

//        val layoutManager =
//            StaggeredGridLayoutManager(
//                resources.getInteger(R.integer.games_columns),
//                StaggeredGridLayoutManager.VERTICAL
//            )

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.gamesSwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener { load() }
        val fab = requireActivity().main_fab
        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.d(LOG_TAG, "onScrolled()")
                viewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        if (!viewModel.isLoaded) load()

        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        newConfig.screenLayout = R.layout.fragment_tab_games
        (binding.gamesRecyclerView.layoutManager as GridLayoutManager).spanCount =
//        (binding.gamesRecyclerView.layoutManager as StaggeredGridLayoutManager).spanCount =
            resources.getInteger(R.integer.games_columns)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    fun load() {
        scope.launch {
            viewModel.load()
            swipeRefreshLayout.isRefreshing = false
        }
    }
}