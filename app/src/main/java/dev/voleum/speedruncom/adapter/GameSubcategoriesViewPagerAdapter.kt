package dev.voleum.speedruncom.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.ui.screen.GameSubcategoriesFragment
import dev.voleum.speedruncom.ui.screen.LeaderboardFragment

class GameSubcategoriesViewPagerAdapter(fragment: GameSubcategoriesFragment,
                                        val variableId: String,
                                        private val subcategoriesIds: List<String>,
                                        private val categoryId: String,
                                        private val gameId: String,
                                        private val trophyAssets: Assets) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = subcategoriesIds.size

    override fun createFragment(position: Int): Fragment =
        LeaderboardFragment().apply {
            arguments = Bundle().apply {
                putString("game", gameId)
                putString("category", categoryId)
                putString("variable", variableId)
                putString("subcategory", subcategoriesIds[position])
                putSerializable("trophyAssets", trophyAssets)
            }
        }
}