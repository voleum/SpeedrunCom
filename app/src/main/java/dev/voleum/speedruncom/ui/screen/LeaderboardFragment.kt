package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.*
import dev.voleum.speedruncom.adapter.recyclerview.OnEntryClickListener
import dev.voleum.speedruncom.databinding.FragmentLeaderboardBinding
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class LeaderboardFragment : AbstractFragment<LeaderboardViewModel, FragmentLeaderboardBinding>() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Snackbar
            .make(binding.leaderboardRecyclerView, R.string.snackbar_unable_to_load, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_action_retry) { load() }
            .show()
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

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
            viewModel.gameId = getString(STRING_KEY_GAME, "")
            viewModel.categoryId = getString(STRING_KEY_CATEGORY, "")
            viewModel.subcategoryId = getString(STRING_KEY_SUBCATEGORY, "")
            viewModel.variableId = getString(STRING_KEY_VARIABLE, "")
            viewModel.trophyAssets = getSerializable(STRING_KEY_TROPHY_ASSETS) as Assets
        }
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.leaderboardRecyclerView
        val layoutManager = LinearLayoutManager(context)

        viewModel.adapter.onEntryClickListener =
            object : OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString(STRING_KEY_RUN, viewModel.data[position].run.id)
                    }
                    findNavController().navigate(R.id.action_run, bundle)
                }
            }

        recyclerView.layoutManager = layoutManager

        if (!viewModel.isLoaded) load()

        return root
    }

    fun load() {
        scope.launch { viewModel.load() }
    }
}