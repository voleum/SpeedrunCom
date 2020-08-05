package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentLeaderboardBinding
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Assets
import kotlinx.android.synthetic.main.fragment_leaderboard.*

class LeaderboardFragment : Fragment() {

    private lateinit var viewModel: LeaderboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LeaderboardViewModel::class.java)
        val binding: FragmentLeaderboardBinding =
            DataBindingUtil.inflate(inflater,
                R.layout.fragment_leaderboard,
                null,
                false)
        arguments?.apply {
            viewModel.gameId = getString("game", "")
            viewModel.categoryId = getString("category", "")
            viewModel.trophyAssets = getSerializable("trophyAssets") as Assets
        }
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.leaderboardRecyclerView
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (viewModel.state) {
            States.CREATED -> {
                viewModel.setListener { checkData() }
                viewModel.load()
            }
            States.PROGRESS -> {
                viewModel.setListener { checkData() }
            }
            States.ERROR -> {
                viewModel.setListener { checkData() }
                Snackbar.make(leaderboard_recycler_view, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        viewModel.state = States.PROGRESS
                        viewModel.load()
                    }
                    .show()
//                viewModel.load()
            }
            States.LOADED -> {

            }
        }
    }
}