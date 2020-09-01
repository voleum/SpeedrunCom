package dev.voleum.speedruncom.ui.nav.notifications

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentNotificationsBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class NotificationsFragment : AbstractFragment<NotificationsViewModel, FragmentNotificationsBinding>() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
//        Snackbar
//            .make(binding.leaderboardRecyclerView, R.string.snackbar_unable_to_load, Snackbar.LENGTH_LONG)
//            .setAction(R.string.snackbar_action_retry) { load() }
//            .show()
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_notifications,
            container,
            false
        )

        binding.viewModel = viewModel

        val recyclerView = binding.notificationsRecyclerView
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation
        ))

        val onScrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {

            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.d("tag", "onScrolled()")
                viewModel.loadMore()
            }
        }
        recyclerView.addOnScrollListener(onScrollListener)

        if (!viewModel.isLoaded) load()

        return binding.root
    }

    private fun load() {
        scope.launch {
            val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
            viewModel.loadApiKey(sharedPreferences)
            val job = launch { viewModel.load() }
            job.join()
            val badge = requireActivity().nav_view.getOrCreateBadge(R.id.navigation_notifications)
            val count = viewModel.data.filter { it.status == "unread" }.size
            if (count > 0) {
                badge.isVisible = true
                badge.number = count
            }
            else {
                badge.isVisible = false
                badge.clearNumber()
            }
        }
    }
}