package dev.voleum.speedruncom.ui.nav.profile

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import dev.voleum.speedruncom.*
import dev.voleum.speedruncom.api.SpeedrunComApi
import kotlinx.android.synthetic.main.fragment_auth.view.*
import kotlinx.coroutines.*
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthFragment : Fragment() {

    private val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d(LOG_TAG, "Exception: ${throwable.message}")
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob() + handler)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_auth, container, false)

        root.button_login.setOnClickListener {
            scope.launch {
                val username = root.auth_edit_text_login.text.toString()
                val password = root.auth_edit_text_password.text.toString()
                val jobProfile = async {
                    auth(mapOf("username" to username, "password" to password))
                }
                val authKey = jobProfile.await()
                val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
                val encryptedApiKey = encrypt(authKey)
                sharedPreferences
                    .edit()
                    .putString(API_KEY_ENCRYPTED_PREF_NAME, encryptedApiKey)
                    .apply()
                val bundle = Bundle()
                bundle.putString(STRING_KEY_API_KEY, encryptedApiKey)
                setFragmentResult("profile", bundle)
            }
        }

        return root
    }

    private suspend fun auth(params: Map<String, String>): String =
        suspendCoroutine {
            Retrofit.Builder()
                .baseUrl("https://www.speedrun.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SpeedrunComApi::class.java)
                .auth(params)
                .enqueue(

                    object : Callback<ResponseBody> {

                        override fun onResponse(call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            try {
                                val doc = Jsoup.parse(response.body()!!.string())
                                it.resume(doc.getElementById("api-key").text())
                            } catch (e: Exception) {
                                onFailure(call, e)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            it.resumeWithException(t)
                        }
                    }
                )
        }
}