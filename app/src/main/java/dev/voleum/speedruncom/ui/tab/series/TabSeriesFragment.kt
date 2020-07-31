package dev.voleum.speedruncom.ui.tab.series

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
import dev.voleum.speedruncom.databinding.FragmentTabSeriesBinding
import dev.voleum.speedruncom.enum.States
import kotlinx.android.synthetic.main.fragment_tab_games.*

class TabSeriesFragment : Fragment() {

    private lateinit var tabSeriesViewModel: TabSeriesViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tabSeriesViewModel = ViewModelProvider(this).get(TabSeriesViewModel::class.java)
        val binding: FragmentTabSeriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_tab_series, null, false)
        binding.viewModel = tabSeriesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = binding.seriesRecyclerView
        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.layoutManager = layoutManager
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
                swipeRefreshLayout.isRefreshing = false
                tabSeriesViewModel.setListener { checkData() }
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        tabSeriesViewModel.state = States.PROGRESS
                        tabSeriesViewModel.load()
                    }
                    .show()
//                tabSeriesViewModel.load()
            }
            States.LOADED -> {
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}