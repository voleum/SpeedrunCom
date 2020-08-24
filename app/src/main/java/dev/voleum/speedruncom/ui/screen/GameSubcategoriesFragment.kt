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
import dev.voleum.speedruncom.model.CategoryEmbed
import dev.voleum.speedruncom.ui.AbstractFragment

class GameSubcategoriesFragment :
    AbstractFragment<GameSubategoriesViewModel, FragmentSubcategoriesBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameSubategoriesViewModel::class.java)
        arguments?.apply {
            viewModel.gameId = getString("game", "")
            viewModel.category = getSerializable("category") as CategoryEmbed
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
        createAdapter()
        return binding.root
    }

    private fun createAdapter() {
        val adapter = GameSubcategoriesViewPagerAdapter(
            this,
            viewModel.variableId,
            viewModel.subcategoriesIds,
            viewModel.category.id,
            viewModel.gameId,
            viewModel.trophyAssets
        )
        binding.subcategoriesViewPager.adapter = adapter
        TabLayoutMediator(
            binding.subcategoriesTabLayout,
            binding.subcategoriesViewPager,
            true,
            false
        ) { tab, position ->
            val tabsText = viewModel.tabsText
            tab.text =
                if (tabsText.isNotEmpty()) tabsText[position]
                else ""
        }.attach()
    }
}