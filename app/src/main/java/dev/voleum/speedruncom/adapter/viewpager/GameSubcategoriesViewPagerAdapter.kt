package dev.voleum.speedruncom.adapter.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.*
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
                putString(STRING_KEY_GAME, gameId)
                putString(STRING_KEY_CATEGORY, categoryId)
                putString(STRING_KEY_VARIABLE, variableId)
                putString(STRING_KEY_SUBCATEGORY, subcategoriesIds[position])
                putSerializable(STRING_KEY_TROPHY_ASSETS, trophyAssets)
            }
        }
}