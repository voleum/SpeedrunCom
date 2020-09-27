package dev.voleum.speedruncom.ui.nav.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.voleum.speedruncom.API_KEY_ENCRYPTED_PREF_NAME
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.STRING_KEY_API_KEY

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val transaction = childFragmentManager.beginTransaction()

        if (sharedPreferences.contains(API_KEY_ENCRYPTED_PREF_NAME)) {
            val apiKey = sharedPreferences.getString(API_KEY_ENCRYPTED_PREF_NAME, "")
            val bundle = Bundle()
            bundle.putString(STRING_KEY_API_KEY, apiKey)
            transaction.add(R.id.profile_container, ExistedProfileFragment::class.java, bundle)
        }
        else {
            transaction.add(R.id.profile_container, AuthFragment())
        }

        transaction.commit()

        setFragmentResultListener()

        return root
    }

    fun setFragmentResultListener() {
        childFragmentManager.setFragmentResultListener("profile", this) { key, bundle ->
            run {
                childFragmentManager.beginTransaction()
                    .replace(
                        R.id.profile_container,
                        ExistedProfileFragment::class.java,
                        bundle
                    )
                    .commit()
            }
        }
    }
}