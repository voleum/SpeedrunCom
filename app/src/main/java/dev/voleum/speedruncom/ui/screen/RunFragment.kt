package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.voleum.speedruncom.STRING_KEY_RUN
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentRunBinding
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class RunFragment : AbstractFragment<RunViewModel, FragmentRunBinding>() {

    val handler = CoroutineExceptionHandler { coroutineContext, throwable ->

    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(RunViewModel::class.java)
        arguments?.apply {
            viewModel.id = getString(STRING_KEY_RUN, "")
        }
        binding =
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
        if (!viewModel.isLoaded) load()
        return binding.root
    }

    private fun load() {
        scope.launch {
            viewModel.load()
        }
    }
}