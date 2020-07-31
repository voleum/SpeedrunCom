package dev.voleum.speedruncom.ui.nav.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GamesViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_games.view.*

class GamesFragment : Fragment() {

    private lateinit var tabsArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tabsArray = resources.getStringArray(R.array.tabs_games)
        val root = inflater.inflate(R.layout.fragment_games, container, false)
        val adapter = GamesViewPagerAdapter(this, tabsArray.size)
        root.games_view_pager.adapter = adapter
        TabLayoutMediator(root.games_tab_layout, root.games_view_pager) { tab, position ->
            tab.text = tabsArray[position]
        }.attach()
        return root
    }
}