package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.*
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GameViewModel : ViewModelObservable() {

    lateinit var id: String

    var isLoaded = false

    val backgroundUrl: String
        get() = game?.assets?.background?.uri ?: ""

    val trophyAssets: Assets
        get() = game!!.assets

    var game: GameEmbed? = null
        @Bindable get
        @Bindable set

    var name: String = ""
        @Bindable get() = game?.names?.international ?: ""
        @Bindable set

    var releaseDate: String = ""
        @Bindable get() = game?.releaseDate ?: ""
        @Bindable set

    var platforms: String = ""
        @Bindable get() {
            var platforms = ""
            game?.platforms?.data?.forEach { platforms += "${it.name}, " }
            return platforms.removeSuffix(", ")
        }
        @Bindable set

    var categories: List<Category> = mutableListOf()
        @Bindable get() = game!!.categories.data
        @Bindable set

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.gameEmbed(id, "platforms,categories").enqueue(object : Callback<DataGameEmbed> {

                override fun onResponse(call: Call<DataGameEmbed>, response: Response<DataGameEmbed>) {
                    game = response.body()!!.data
                    notifyChange()
                    //TODO: exception if game not founded
                    Log.d("tag", "load onResponse()")
                    isLoaded = true
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<DataGameEmbed>, t: Throwable) {
                    t.stackTrace
                    t.message
                    Log.d("tag", "load onError()")
                    it.resumeWithException(t)
                }
            })
        }
    }
}