package dev.voleum.speedruncom.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.ui.nav.games.GamesFragment
import dev.voleum.speedruncom.ui.tab.games.TabGamesFragment
import dev.voleum.speedruncom.ui.tab.series.TabSeriesFragment

class GamesViewPagerAdapter(fragment: GamesFragment, private val itemsCount: Int) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TabGamesFragment()
            else -> TabSeriesFragment()
        }
    }
}