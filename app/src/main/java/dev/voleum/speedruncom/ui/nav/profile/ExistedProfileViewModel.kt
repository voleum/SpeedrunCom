package dev.voleum.speedruncom.ui.nav.profile

import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.DataUser
import dev.voleum.speedruncom.model.User
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ExistedProfileViewModel : ViewModelObservable() {

    var isLoaded = false

    var user: User? = null
        @Bindable get
        @Bindable set

    val name: String
        @Bindable get() = user?.names?.international ?: ""

    val country: String
        get() = user?.location?.country?.code ?: ""

//    val region: String
//        @Bindable get() = user?.location?.region?.names?.international ?: ""

    val twitchLink: String
        @Bindable get() = user?.twitch?.uri ?: ""

    val hitboxLink: String
        @Bindable get() = user?.hitbox?.uri ?: ""

    val youtubeLink: String
        @Bindable get() = user?.youtube?.uri ?: ""

    val twitterLink: String
        @Bindable get() = user?.twitter?.uri ?: ""

    val speedrunsliveLink: String
        @Bindable get() = user?.speedrunslive?.uri ?: ""

    suspend fun load(apiKey: String) {
        suspendCoroutine<Unit> {
            API.profile(apiKey).enqueue(object : Callback<DataUser> {

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    try {
                        user = response.body()!!.data
                        isLoaded = true
                        notifyChange()
                        it.resume(Unit)
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