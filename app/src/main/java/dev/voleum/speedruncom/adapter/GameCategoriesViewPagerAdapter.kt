package dev.voleum.speedruncom.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.Category
import dev.voleum.speedruncom.ui.screen.GameFragment
import dev.voleum.speedruncom.ui.screen.GameSubcategoriesFragment

class GameCategoriesViewPagerAdapter(fragment: GameFragment,
                                     val categories: List<Category>,
                                     val gameId: String,
                                     val trophyAssets: Assets) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment =
        GameSubcategoriesFragment().apply {
            arguments = Bundle().apply {
                putString("game", gameId)
                putString("category", categories[position].id)
                putSerializable("trophyAssets", trophyAssets)
            }
        }
}