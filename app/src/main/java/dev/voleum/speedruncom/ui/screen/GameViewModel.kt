package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.DataGame
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameViewModel : ViewModelObservable() {

    lateinit var id: String

    lateinit var loadListener: () -> Unit

    var state = States.CREATED

    var backgroundUrl: String = ""
        get() = game?.assets?.background?.uri ?: ""

    var game: Game? = null
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
            return if (game != null) {
                var platforms = ""
                game?.platforms?.forEach { platforms += "$it, " }
                platforms.removeSuffix(", ")
            } else ""
        }
        @Bindable set

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun load() {
        API.games(id).enqueue(object : Callback<DataGame> {
            override fun onResponse(call: Call<DataGame>, response: Response<DataGame>) {
//                adapter.replaceItems(response.body()!!.data)
//                pagination = response.body()!!.pagination
                game = response.body()!!.data
                notifyChange()
                //TODO: exception if game not founded
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<DataGame>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError()")
                loadListener()
            }

        })
    }

}