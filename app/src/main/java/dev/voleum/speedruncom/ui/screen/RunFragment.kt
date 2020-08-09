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
import dev.voleum.speedruncom.databinding.FragmentRunBinding
import dev.voleum.speedruncom.enum.States
import kotlinx.android.synthetic.main.fragment_tab_games.*

class RunFragment : Fragment() {

    private lateinit var viewModel: RunViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        arguments?.apply {
            viewModel.id = getString("run", "")
        }
        val binding: FragmentRunBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_run,
                null,
                false
            )
        binding.viewModel = viewModel
        val recyclerView = binding.runRecyclerView
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        checkData()
        return binding.root
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
//                swipeRefreshLayout.isRefreshing = false
                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        viewModel.state = States.PROGRESS
                        viewModel.load()
                    }
                    .show()
//                gamesViewModel.load()
            }
            States.LOADED -> {

            }
        }
    }
}