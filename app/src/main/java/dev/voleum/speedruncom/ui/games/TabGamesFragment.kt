package dev.voleum.speedruncom.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
        val recyclerView: RecyclerView = root.findViewById(R.id.games_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.adapter = gamesViewModel.adapter
        swipeRefreshLayout = root.findViewById(R.id.games_swipe_refresh_layout)
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
                gamesViewModel.load()
            }
            States.LOADED -> {
                gamesViewModel.adapter.addItems(gamesViewModel.data)
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }
}