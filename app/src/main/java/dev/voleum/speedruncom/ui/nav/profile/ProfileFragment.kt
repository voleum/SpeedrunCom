package dev.voleum.speedruncom.ui.nav.profile

import android.content.Context
import android.content.SharedPreferences
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
import dev.voleum.speedruncom.API_KEY_PREF_NAME
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.SpeedrunCom
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val transaction = childFragmentManager.beginTransaction()

        if (sharedPreferences.contains(API_KEY_PREF_NAME))
            transaction.add(R.id.profile_container, ProfileFragment())
        else transaction.add(R.id.profile_container, AuthFragment())

        transaction.commit()

        return root
    }
}