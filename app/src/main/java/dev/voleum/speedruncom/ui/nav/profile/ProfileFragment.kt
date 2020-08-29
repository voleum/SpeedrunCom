package dev.voleum.speedruncom.ui.nav.profile

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.voleum.speedruncom.R
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var moreViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        moreViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        root.btn_auth_page.setOnClickListener {
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

        return root
    }
}