package dev.voleum.speedruncom.ui.nav.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import dev.voleum.speedruncom.R
import kotlinx.android.synthetic.main.fragment_auth.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AuthFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_auth, container, false)

        root.open_auth_page.setOnClickListener {
            val url = "https://www.speedrun.com/api/auth"
            val customTabsIntent =
                CustomTabsIntent.Builder()
                    .setToolbarColor(resources.getColor(R.color.colorPrimary))
                    .setCloseButtonIcon(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_baseline_arrow_back_24,
                            null
                        )!!.toBitmap()
                    )
                    .build()
            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
        }

        root.button_auth.setOnClickListener {
            GlobalScope.launch {

            }
        }

        return root
    }

    private fun auth() {

    }
}