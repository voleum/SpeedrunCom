package dev.voleum.speedruncom.ui.tab.games

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TabGamesViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Game>) {
            if (recyclerView.adapter is GamesRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as GamesRecyclerViewAdapter
                if (adapter.items != list)
                    adapter.replaceItems(list)
            }
        }
    }

    var isGamesLoaded = false
        @Bindable get

    lateinit var pagination: Pagination

    var adapter = GamesRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Game> = adapter.items
        @Bindable get

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.games().enqueue(object : Callback<GameList> {
                override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                    adapter.replaceItems(response.body()!!.data)
                    pagination = response.body()!!.pagination
                    Log.d("tag", "load onResponse()")
                    isGamesLoaded = true
                    notifyPropertyChanged(BR.gamesLoaded)
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<GameList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    Log.d("tag", "load onError()")
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.games(pagination.offset + pagination.size).enqueue(object : Callback<GameList> {
            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                pagination = response.body()!!.pagination
                adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                Log.d("tag", "loadMore onResponse(); data.size: ${data.size}; adapter.items.size: ${adapter.items.size}")
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                Log.d("tag", "loadMore onError()")
                //TODO: do something
            }
        })
    }
}