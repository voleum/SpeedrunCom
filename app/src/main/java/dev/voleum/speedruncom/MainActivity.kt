package dev.voleum.speedruncom

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.voleum.speedruncom.ui.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),
    ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private val backStack = Stack<Int>()

    private val fragments = listOf(
        BaseFragment.newInstance(R.layout.content_home, R.id.toolbar_home, R.id.nav_host_home),
        BaseFragment.newInstance(R.layout.content_search, R.id.toolbar_search, R.id.nav_host_search),
        BaseFragment.newInstance(R.layout.content_games, R.id.toolbar_games, R.id.nav_host_games),
        BaseFragment.newInstance(R.layout.content_more, R.id.toolbar_more, R.id.nav_host_more)
    )

    private val indexToPage = mapOf(
        0 to R.id.navigation_home,
        1 to R.id.navigation_games,
        2 to R.id.navigation_more
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        main_pager.registerOnPageChangeCallback(OnPageChangeCallback())
        main_pager.addOnPageChangeListener(this)
        main_pager.adapter = MainViewPagerAdapter()
//        main_pager.post(this::checkDeepLink)
        main_pager.offscreenPageLimit = fragments.size

        nav_view.setOnNavigationItemSelectedListener(this)
        nav_view.setOnNavigationItemReselectedListener(this)

        if (backStack.empty()) backStack.push(0)
    }

    override fun onBackPressed() {
        val fragment = fragments[main_pager.currentItem]
        val hadNestedFragments = fragment.onBackPressed()
        if (!hadNestedFragments) {
            if (backStack.size > 1) {
                backStack.pop()
                main_pager.currentItem = backStack.peek()
            } else super.onBackPressed()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        Log.d("tag", "onPageSelected(); position: $position")
        val itemId = indexToPage[position] ?: R.id.navigation_home
        if (nav_view.selectedItemId != itemId) nav_view.selectedItemId = itemId
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d("tag", "onNavigationItemSelected(); itemId: ${item.itemId}")
        val position = indexToPage.values.indexOf(item.itemId)
        if (main_pager.currentItem != position) setItem(position)
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        Log.d("tag", "onNavigationItemReselected(); itemId: ${item.itemId}")
        val position = indexToPage.values.indexOf(item.itemId)
        val fragment = fragments[position]
        fragment.popToRoot()
    }

    private fun setItem(position: Int) {
        main_pager.currentItem = position
        backStack.push(position)
    }

    private fun checkDeepLink() {
        fragments.forEachIndexed { index, baseFragment ->
            val hasDeepLink = baseFragment.handleDeepLink(intent)
            if (hasDeepLink) setItem(index)
        }
    }

//    inner class MainViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
//        override fun createFragment(position: Int): Fragment = fragments[position]
//        override fun getItemCount(): Int = fragments.size
//    }

    inner class MainViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getItem(position: Int): Fragment = fragments[position]
        override fun getCount(): Int = fragments.size
    }

//    inner class OnPageChangeCallback : ViewPager2.OnPageChangeCallback() {
//
//        override fun onPageScrollStateChanged(state: Int) {}
//
//        override fun onPageScrolled(
//            position: Int,
//            positionOffset: Float,
//            positionOffsetPixels: Int
//        ) {}
//
//        override fun onPageSelected(position: Int) {
//            Log.d("tag", "onPageSelected(); position: $position")
//            val itemId = indexToPage[position] ?: R.id.navigation_home
//            if (nav_view.selectedItemId != itemId) nav_view.selectedItemId = itemId
//        }
//    }
}