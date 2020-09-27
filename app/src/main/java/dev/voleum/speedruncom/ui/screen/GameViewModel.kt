package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryEmbed
import dev.voleum.speedruncom.model.DataGameEmbed
import dev.voleum.speedruncom.model.GameEmbed
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GameViewModel : AbstractViewModel() {

    lateinit var id: String

    val backgroundUrl: String
        get() = game?.assets?.background?.uri ?: ""

    val trophyAssets: Assets
        get() = game!!.assets

    var game: GameEmbed? = null
        @Bindable get
        @Bindable set

    val name: String
        @Bindable get() = game?.names?.international ?: ""

    val releaseDate: String
        @Bindable get() = game?.releaseDate ?: ""

    val platforms: String
        @Bindable get() {
            var platforms = ""
            game?.platforms?.data?.forEach { platforms += "${it.name}, " }
            return platforms.removeSuffix(", ")
        }

    var categories: List<CategoryEmbed> = mutableListOf()
        @Bindable get() = game!!.categories.data
        @Bindable set

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.gameEmbed(id, "platforms,categories,categories.variables").enqueue(object : Callback<DataGameEmbed> {

                override fun onResponse(call: Call<DataGameEmbed>, response: Response<DataGameEmbed>) {
                    try {
                        game = response.body()!!.data
                        notifyChange()
                        isLoaded = true
                        it.resume(Unit)
                    } catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<DataGameEmbed>, t: Throwable) {
                    t.stackTrace
                    t.message
                    it.resumeWithException(t)
                }
            })
        }
    }
}