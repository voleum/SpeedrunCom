package dev.voleum.speedruncom.ui.nav.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.LOG_TAG
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.STRING_KEY_RUN
import dev.voleum.speedruncom.adapter.recyclerview.OnEntryClickListener
import dev.voleum.speedruncom.databinding.FragmentHomeBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class HomeFragment : AbstractFragment<HomeViewModel, FragmentHomeBinding>() {

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main + handler)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )
        binding.viewModel = viewModel

        val recyclerView = binding.homeLatestRunsRecyclerView

        viewModel.adapter.onEntryClickListener =
            object : OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    val bundle = Bundle().apply {
                        putString(STRING_KEY_RUN, viewModel.data[position].id)
                    }
                    findNavController().navigate(R.id.action_run, bundle)
                }
            }

        val layoutManager = LinearLayoutManager(requireContext())

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        )
        )
        recyclerView.setHasFixedSize(true)

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.d(LOG_TAG, "onScrolled()")
                viewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        if (!viewModel.isLoaded) load()

        return binding.root
    }

    fun load() {
        scope.launch { viewModel.load() }
    }
}