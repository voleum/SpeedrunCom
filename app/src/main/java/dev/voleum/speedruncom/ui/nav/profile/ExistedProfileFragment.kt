package dev.voleum.speedruncom.ui.nav.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentExistiedProfileBinding
import dev.voleum.speedruncom.decrypt
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.*

class ExistedProfileFragment : AbstractFragment<ExistedProfileViewModel, FragmentExistiedProfileBinding>() {

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Snackbar
            .make(binding.root, R.string.snackbar_unable_to_load, Snackbar.LENGTH_LONG)
            .setAction(R.string.snackbar_action_retry) { load() }
            .show()
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ExistedProfileViewModel::class.java)
//        arguments?.apply {
//            viewModel.apiKey = getString("api_key", "")
//        }

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_existied_profile,
            container,
            false
        )

        binding.viewModel = viewModel

        load()

        return binding.root
    }

    fun load() {
        val apiKey = decrypt(requireArguments().getString("api_key", ""))
        scope.launch { viewModel.load(String(apiKey)) }
    }
}