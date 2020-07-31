package dev.voleum.speedruncom.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import dev.voleum.speedruncom.rootDestinations

class BaseFragment : Fragment() {

    companion object {

        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_TOOLBAR = "toolbar_key"
        private const val KEY_NAV_HOST = "nav_host_key"

        fun newInstance(layoutRes: Int, toolbarId: Int, navHostId: Int) = BaseFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_LAYOUT, layoutRes)
                putInt(KEY_TOOLBAR, toolbarId)
                putInt(KEY_NAV_HOST, navHostId)
            }
        }
    }
    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    private val appBarConfig = AppBarConfiguration(rootDestinations)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            toolbarId = it.getInt(KEY_TOOLBAR)
            navHostId = it.getInt(KEY_NAV_HOST)
        } ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        if (layoutRes == defaultInt) null
        else inflater.inflate(layoutRes, container, false)

    override fun onStart() {
        super.onStart()

        if (toolbarId == defaultInt || navHostId == defaultInt) return

        val toolbar: Toolbar = requireActivity().findViewById(toolbarId)
        val navController = requireActivity().findNavController(navHostId)

        NavigationUI.setupWithNavController(toolbar, navController, appBarConfig)
    }

    fun onBackPressed(): Boolean =
        requireActivity()
            .findNavController(navHostId)
            .navigateUp(appBarConfig)

    fun popToRoot() {
        val navController = requireActivity().findNavController(navHostId)
        navController.popBackStack(navController.graph.startDestination, false)
    }

    fun handleDeepLink(intent: Intent) =
        requireActivity()
            .findNavController(navHostId)
            .handleDeepLink(intent)
}