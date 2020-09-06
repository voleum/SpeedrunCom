package dev.voleum.speedruncom.ui.nav.notifications

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.EndlessRecyclerViewScrollListener
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.NotificationsRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentNotificationsBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class NotificationsFragment : AbstractFragment<NotificationsViewModel, FragmentNotificationsBinding>() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->

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

        viewModel.adapter.onEntryClickListener =
            object : NotificationsRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    when (viewModel.adapter.items[position].item.rel) {
                        "run" -> {
                            val link = viewModel.data[position].links.find { it.rel == "run" }
                            val id = link?.uri?.substringAfterLast("https://www.speedrun.com/api/v1/runs/") ?: ""
                            val bundle = Bundle().apply {
                                putString("run", id)
                            }
                            findNavController().navigate(R.id.action_run, bundle)
                        }
                        "game" -> {
                            val link = viewModel.data[position].links.find { it.rel == "game" }
                            val id = link?.uri?.substringAfterLast("https://www.speedrun.com/api/v1/games/") ?: ""
                            val bundle = Bundle().apply {
                                putString("game", id)
                            }
                            findNavController().navigate(R.id.action_game, bundle)
                        }
                        "post", "guide" -> {
                            val customTabsIntent =
                                CustomTabsIntent.Builder()
                                    .setToolbarColor(resources.getColor(R.color.colorPrimary))
                                    .setCloseButtonIcon(
                                        ResourcesCompat.getDrawable(
                                            resources,
                                            R.drawable.ic_baseline_arrow_back_24,
                                            null
                                        )!!.toBitmap()
                                    )
                                    .build()
                            customTabsIntent.launchUrl(requireContext(), Uri.parse(viewModel.data[position].item.uri))
                        }
                    }
                }
            }

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
                setBadge()
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
            setBadge()
        }
    }

    private fun setBadge() {
        val badge = requireActivity().nav_view.getOrCreateBadge(R.id.navigation_notifications)
        val count = viewModel.data.filter { it.status == "unread" }.size
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
//            badge.maxCharacterCount = 2
        }
        else {
            badge.isVisible = false
            badge.clearNumber()
        }
    }
}