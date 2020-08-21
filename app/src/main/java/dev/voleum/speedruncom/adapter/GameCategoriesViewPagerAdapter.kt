package dev.voleum.speedruncom.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
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
                putString("game", gameId)
                putSerializable("category", categories[position])
                putSerializable("trophyAssets", trophyAssets)
            }
        }
}