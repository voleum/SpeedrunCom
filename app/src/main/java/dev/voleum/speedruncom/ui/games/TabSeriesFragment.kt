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

    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        seriesViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        val binding: FragmentTabSeriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_series, null, false)
        binding.viewModel = seriesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = binding.seriesRecyclerView
        val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = seriesViewModel.adapter
        recyclerView.itemAnimator!!.changeDuration = 0
        swipeRefreshLayout = binding.seriesSwipeRefreshLayout
        val fab = binding.gamesFab
                fab.setOnClickListener { recyclerView.smoothScrollToPosition(0) }

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                seriesViewModel.state = States.PROGRESS
                Log.d("tag", "onScrolled()")
                seriesViewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)
        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (seriesViewModel.state) {
            States.CREATED -> {
                seriesViewModel.setListener { checkData() }
                seriesViewModel.load()
            }
            States.PROGRESS -> {
                seriesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                seriesViewModel.setListener { checkData() }
                seriesViewModel.load()
            }
            States.LOADED -> {
                seriesViewModel.adapter.replaceItems(seriesViewModel.data)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}