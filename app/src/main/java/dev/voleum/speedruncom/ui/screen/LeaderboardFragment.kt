package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.LeaderboardRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentLeaderboardBinding
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LeaderboardFragment : AbstractFragment<LeaderboardViewModel, FragmentLeaderboardBinding>() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LeaderboardViewModel::class.java)
        binding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_leaderboard,
                null,
                false)
        arguments?.apply {
            viewModel.gameId = getString("game", "")
            viewModel.categoryId = getString("category", "")
            viewModel.subcategoryId = getString("subcategory", "")
            viewModel.variableId = getString("variable", "")
            viewModel.trophyAssets = getSerializable("trophyAssets") as Assets
        }
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.leaderboardRecyclerView
        val layoutManager = LinearLayoutManager(context)

        viewModel.adapter.onEntryClickListener =
            object : LeaderboardRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString("run", viewModel.data[position].run.id)
                    }
                    findNavController().navigate(R.id.action_run, bundle)
                }
            }

        recyclerView.layoutManager = layoutManager

        scope.launch {
            if (!viewModel.isLeaderboardLoaded) {
                val jobLeaderboard = launch { viewModel.load() }
                jobLeaderboard.join()
            }
            if (!viewModel.isUsersLoaded)
                launch { viewModel.loadUsers() }
        }

        return root
    }
}