package dev.voleum.speedruncom.ui.tab.series

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
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.SeriesRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentTabSeriesBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class TabSeriesFragment : AbstractFragment<TabSeriesViewModel, FragmentTabSeriesBinding>() {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(TabSeriesViewModel::class.java)
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_tab_series,
                null,
                false
            )
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.seriesRecyclerView

        viewModel.adapter.onEntryClickListener =
            object : SeriesRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString("series", viewModel.data[position].id)
                    }
                    findNavController().navigate(R.id.action_games_series, bundle)
                }
            }

        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.games_columns))

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.seriesSwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            scope.launch {
                viewModel.load()
                swipeRefreshLayout.isRefreshing = false
            }
        }
        val fab = binding.gamesFab
        fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.d("tag", "onScrolled()")
                viewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        if (!viewModel.isSeriesLoaded)
            scope.launch {
                viewModel.load()
            }

        return root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        (binding.seriesRecyclerView.layoutManager as GridLayoutManager).spanCount =
            resources.getInteger(R.integer.games_columns)
    }

    override fun onPause() {
        super.onPause()
        Log.d("tag", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("tag", "onStop")
        job.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("tag", "onDestroy")
        scope.cancel()
    }
}