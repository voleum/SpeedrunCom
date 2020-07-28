package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.GamesAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Game>) {
            if (recyclerView.adapter is GamesAdapter)
                (recyclerView.adapter as GamesAdapter).addItems(list)
        }
    }

    var adapter = GamesAdapter()
        @Bindable get
        @Bindable set

    lateinit var loadListener: () -> Unit

    var data: List<Game> = adapter.items
        @Bindable get

    var state = States.CREATED

    fun load() {
        API.games().enqueue(object : Callback<GameList> {
            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                data = response.body()!!.data
                state = States.LOADED
                loadListener()
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                loadListener()
            }

        })
    }

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun onRefresh() {
        state = States.PROGRESS
        load()
    }
}