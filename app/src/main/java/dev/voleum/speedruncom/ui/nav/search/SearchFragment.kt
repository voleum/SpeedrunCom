package dev.voleum.speedruncom.ui.nav.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.SearchRecyclerViewAdapter
import dev.voleum.speedruncom.databinding.FragmentSearchBinding
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.model.TYPE_GAME
import dev.voleum.speedruncom.model.TYPE_SERIES
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class SearchFragment :
    AbstractFragment<SearchViewModel, FragmentSearchBinding>(),
    SearchView.OnQueryTextListener {

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Snackbar
            .make(
                binding.searchRecyclerView,
                R.string.snackbar_unable_to_load,
                Snackbar.LENGTH_LONG
            )
            .setAction(R.string.snackbar_action_retry) { search() }
            .show()
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
//        arguments?.apply {
//            viewModel.seriesId = getString("series", "")
//        }
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_search,
                null,
                false
            )
        binding.viewModel = viewModel
        val root = binding.root
        val recyclerView = binding.searchRecyclerView

        viewModel.adapter.onEntryClickListener =
            object : SearchRecyclerViewAdapter.OnEntryClickListener {
                override fun onEntryClick(view: View?, position: Int) {
                    when (viewModel.adapter.searchResult.getType(position)) {
                        TYPE_SERIES -> {
                            val bundle = Bundle().apply {
                                putString("series", (viewModel.data[position] as Series).id)
                            }
                            findNavController().navigate(R.id.action_series, bundle)
                        }
                        TYPE_GAME -> {
                            val bundle = Bundle().apply {
                                putString("game", (viewModel.data[position] as Game).id)
                            }
                            findNavController().navigate(R.id.action_game, bundle)
                        }
                    }
                }
            }

        val layoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator!!.changeDuration = 0

        binding.searchSearchView.setOnQueryTextListener(this)
//        binding.searchSearchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
//            requireActivity().nav_view.visibility =
//                if (hasFocus) View.GONE
//                else View.VISIBLE
//        }

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("tag", "onQueryTextSubmit: $query")
        viewModel.searchString = query!!
        search()
        val ime =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        ime.hideSoftInputFromWindow(requireView().windowToken, 0)
        requireActivity().currentFocus?.clearFocus()
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("tag", "onQueryTextChange: $newText")
        return true
    }

    fun search() {
        viewModel.adapter.clearResult()
        scope.launch {
            launch { viewModel.findSeries() }
            launch { viewModel.findGames() }
        }
    }
}