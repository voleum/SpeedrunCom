package dev.voleum.speedruncom.ui.nav.profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import dev.voleum.speedruncom.API_KEY_ENCRYPTED_PREF_NAME
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.encrypt
import dev.voleum.speedruncom.model.DataUser
import dev.voleum.speedruncom.model.User
import kotlinx.android.synthetic.main.fragment_auth.view.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthFragment : Fragment() {

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("auth", "Exception: ${throwable.message}")
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

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
            scope.launch {
                val authKey = root.auth_edit_text_key.text.toString()
                val jobProfile = async { auth(authKey) }
                val profile: User = jobProfile.await()
                Log.d("auth", "User: ${profile.names.international}")
                val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val encryptedApiKey = encrypt(authKey)
                sharedPreferences
                    .edit()
                    .putString(API_KEY_ENCRYPTED_PREF_NAME, encryptedApiKey)
                    .apply()
                val bundle = Bundle()
                bundle.putString("api_key", encryptedApiKey)
                setFragmentResult("profile", bundle)
            }
        }

        return root
    }

    private suspend fun auth(key: String): User {
        return suspendCoroutine {
            API.profile(key).enqueue(object : Callback<DataUser> {

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    try {
                        it.resume(response.body()!!.data)
                    }
                    catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<DataUser>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}