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
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentTabSeriesBinding
import dev.voleum.speedruncom.enum.States

class TabSeriesFragment : Fragment() {

    private lateinit var tabSeriesViewModel: TabSeriesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tabSeriesViewModel = ViewModelProvider(this).get(TabSeriesViewModel::class.java)
        val binding: FragmentTabSeriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_series, null, false)
        binding.viewModel = tabSeriesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = binding.seriesRecyclerView
        val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = tabSeriesViewModel.adapter
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.seriesSwipeRefreshLayout
        val fab = binding.gamesFab
                fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                tabSeriesViewModel.state = States.PROGRESS
                Log.d("tag", "onScrolled()")
                tabSeriesViewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (tabSeriesViewModel.state) {
            States.CREATED -> {
                tabSeriesViewModel.setListener { checkData() }
                tabSeriesViewModel.load()
            }
            States.PROGRESS -> {
                tabSeriesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                tabSeriesViewModel.setListener { checkData() }
                tabSeriesViewModel.load()
            }
            States.LOADED -> {
                tabSeriesViewModel.adapter.replaceItems(tabSeriesViewModel.data)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}