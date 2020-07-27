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
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentTabSeriesBinding
import dev.voleum.speedruncom.enum.States

class TabSeriesFragment : Fragment() {

    private lateinit var seriesViewModel: SeriesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        seriesViewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
        val binding: FragmentTabSeriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_tab_series, null, false)
        binding.viewModel = seriesViewModel
        val root = binding.root
        val recyclerView: RecyclerView = root.findViewById(R.id.games_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.games_columns))
        recyclerView.adapter = seriesViewModel.adapter
        checkData()
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (seriesViewModel.state) {
            States.CREATED -> {
                seriesViewModel.setListener { checkData() }
                seriesViewModel.load()
            }
            States.PROGRESS -> {
                seriesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                seriesViewModel.setListener { checkData() }
                seriesViewModel.load()
            }
            States.LOADED -> {
                seriesViewModel.adapter.addItems(seriesViewModel.data)
            }
        }
    }
}