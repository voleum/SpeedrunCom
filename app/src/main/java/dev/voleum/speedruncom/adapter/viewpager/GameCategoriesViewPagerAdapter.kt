package dev.voleum.speedruncom.adapter.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.STRING_KEY_CATEGORY
import dev.voleum.speedruncom.STRING_KEY_GAME
import dev.voleum.speedruncom.STRING_KEY_TROPHY_ASSETS
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryEmbed
import dev.voleum.speedruncom.ui.screen.GameFragment
import dev.voleum.speedruncom.ui.screen.GameSubcategoriesFragment

class GameCategoriesViewPagerAdapter(fragment: GameFragment,
                                     private val categories: List<CategoryEmbed>,
                                     private val gameId: String,
                                     private val trophyAssets: Assets) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment =
        GameSubcategoriesFragment().apply {
            arguments = Bundle().apply {
                putString(STRING_KEY_GAME, gameId)
                putSerializable(STRING_KEY_CATEGORY, categories[position])
                putSerializable(STRING_KEY_TROPHY_ASSETS, trophyAssets)
            }
        }
}