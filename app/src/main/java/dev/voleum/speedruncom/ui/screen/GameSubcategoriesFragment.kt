package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GameSubcategoriesViewPagerAdapter
import dev.voleum.speedruncom.databinding.FragmentSubcategoriesBinding
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.ui.AbstractFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GameSubcategoriesFragment :
    AbstractFragment<GameSubategoriesViewModel, FragmentSubcategoriesBinding>() {

    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameSubategoriesViewModel::class.java)
        arguments?.apply {
            viewModel.gameId = getString("game", "")
            viewModel.categoryId = getString("category", "")
            viewModel.trophyAssets = getSerializable("trophyAssets") as Assets
        }
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_subcategories,
                container,
                false
            )
        binding.viewModel = viewModel

        scope.launch {
            if (!viewModel.isSubcategoriesLoaded) {
                val jobSubcategories = launch { viewModel.load() }
                jobSubcategories.join()
            }
            if (binding.subcategoriesViewPager.adapter == null)
                createAdapter()
        }

//        checkData()
        return binding.root
    }

    private fun createAdapter() {
        val adapter = GameSubcategoriesViewPagerAdapter(
            this,
            viewModel.variableId,
            viewModel.getSubcategoriesIds(),
            viewModel.categoryId,
            viewModel.gameId,
            viewModel.trophyAssets
        )
        binding.subcategoriesViewPager.adapter = adapter
        TabLayoutMediator(
            binding.subcategoriesTabLayout,
            binding.subcategoriesViewPager
        ) { tab, position ->
            val tabsText = viewModel.getTabsText()
            tab.text =
                if (tabsText.isNotEmpty()) tabsText[position]
                else ""
        }.attach()
    }

//    private fun checkData() {
////        if (view == null) return
//
//        when (viewModel.state) {
//            States.CREATED -> {
//                viewModel.setListener { checkData() }
//                viewModel.load()
//            }
//            States.PROGRESS -> {
//                viewModel.setListener { checkData() }
//            }
//            States.ERROR -> {
//                viewModel.setListener { checkData() }
//                Snackbar.make(games_swipe_refresh_layout, "Unable to load", Snackbar.LENGTH_LONG)
//                    .setAction("Retry") {
//                        viewModel.state = States.PROGRESS
//                        viewModel.load()
//                    }
//                    .show()
//            }
//            States.LOADED -> {
//                val adapter = GameSubcategoriesViewPagerAdapter(
//                    this,
//                    viewModel.variableId,
//                    viewModel.getSubcategoriesIds(),
//                    viewModel.categoryId,
//                    viewModel.gameId,
//                    viewModel.trophyAssets
//                )
//                binding.subcategoriesViewPager.adapter = adapter
//                TabLayoutMediator(
//                    binding.subcategoriesTabLayout,
//                    binding.subcategoriesViewPager
//                ) { tab, position ->
//                    val tabsText = viewModel.getTabsText()
//                    tab.text =
//                        if (tabsText.isNotEmpty()) tabsText[position]
//                        else ""
//                }.attach()
//            }
//        }
//    }
}