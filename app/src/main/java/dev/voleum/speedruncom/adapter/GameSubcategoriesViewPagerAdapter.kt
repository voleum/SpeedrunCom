package dev.voleum.speedruncom.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryValues
import dev.voleum.speedruncom.ui.screen.GameSubcategoriesFragment
import dev.voleum.speedruncom.ui.screen.LeaderboardFragment

class GameSubcategoriesViewPagerAdapter(fragment: GameSubcategoriesFragment,
                                        val subcategoriesIds: List<String>,
                                        val values: Map<String, CategoryValues>,
                                        val categoryId: String,
                                        val gameId: String,
                                        val trophyAssets: Assets) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = values.size

    override fun createFragment(position: Int): Fragment =
        LeaderboardFragment().apply {
            arguments = Bundle().apply {
                putString("game", gameId)
                putString("category", categoryId)
                putString("subcategory", subcategoriesIds[position])
                putSerializable("trophyAssets", trophyAssets)
            }
        }
}