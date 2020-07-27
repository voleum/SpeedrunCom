package dev.voleum.speedruncom.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.voleum.speedruncom.ui.games.GamesFragment
import dev.voleum.speedruncom.ui.games.TabGamesFragment
import dev.voleum.speedruncom.ui.games.TabSeriesFragment

class GamesTabAdapter(fragment: GamesFragment, private val itemsCount: Int) : FragmentStateAdapter(fragment) {

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