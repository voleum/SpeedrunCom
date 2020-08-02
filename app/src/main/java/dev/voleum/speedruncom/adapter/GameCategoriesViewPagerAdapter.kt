package dev.voleum.speedruncom.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.ui.screen.GameFragment
import dev.voleum.speedruncom.ui.screen.LeaderboardFragment

class GameCategoriesViewPagerAdapter(fragment: GameFragment, val itemsCount: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = itemsCount

    override fun createFragment(position: Int): Fragment = LeaderboardFragment()
}