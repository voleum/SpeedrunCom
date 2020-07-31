package dev.voleum.speedruncom.ui.games

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabGamesViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Game>) {
            if (recyclerView.adapter is GamesRecyclerViewAdapter)
                (recyclerView.adapter as GamesRecyclerViewAdapter).replaceItems(list)
        }
    }

    lateinit var loadListener: () -> Unit

    lateinit var pagination: Pagination

    var state = States.CREATED

    var adapter = GamesRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Game> = adapter.items
        @Bindable get

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun onRefresh() {
        state = States.PROGRESS
        load()
    }

    fun load() {
        API.games().enqueue(object : Callback<GameList> {
            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                data = response.body()!!.data
                pagination = response.body()!!.pagination
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError()")
                loadListener()
            }

        })
    }

    fun loadMore() {
        API.games(pagination.offset + pagination.size).enqueue(object : Callback<GameList> {
            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                pagination = response.body()!!.pagination
                adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                state = States.LOADED
                Log.d("tag", "loadMore onResponse(); data.size: ${data.size}; adapter.items.size: ${adapter.items.size}")
//                loadListener()
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "loadMore onError()")
                //TODO: doing something
//                loadListener()
            }

        })
    }
}