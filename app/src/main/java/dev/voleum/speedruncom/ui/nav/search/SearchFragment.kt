package dev.voleum.speedruncom.ui.nav.search

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.adapter.SearchRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentSearchBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class SearchFragment : AbstractFragment<SearchViewModel, FragmentSearchBinding>() {

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
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
//        arguments?.apply {
//            viewModel.seriesId = getString("series", "")
//        }
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                null,
                false
            )
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.gamesRecyclerView

        viewModel.adapter.onEntryClickListener =
            object : SearchRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
//                        putString("game", viewModel.data[position].id)
                    }
                    findNavController().navigate(R.id.action_game, bundle)
                }
            }

        val layoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0

//        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
//
//            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                Log.d("tag", "onScrolled()")
//                viewModel.loadMore()
//            }
//        }
//        recyclerView.addOnScrollListener(onScrollListener)

        if (!viewModel.isLoaded) load()

        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        newConfig.screenLayout = R.layout.fragment_tab_games
//        (binding.gamesRecyclerView.layoutManager as GridLayoutManager).spanCount =
        (binding.gamesRecyclerView.layoutManager as StaggeredGridLayoutManager).spanCount =
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