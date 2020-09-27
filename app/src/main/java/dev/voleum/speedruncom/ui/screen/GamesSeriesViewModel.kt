package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.recyclerview.GamesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GamesSeriesViewModel : AbstractViewModel() {

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

    lateinit var seriesId: String

    lateinit var pagination: Pagination

    var adapter = GamesRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Game> = adapter.items
        @Bindable get

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.gamesSeries(seriesId).enqueue(object : Callback<GameList> {
                override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                    try {
                        adapter.replaceItems(response.body()!!.data)
                        pagination = response.body()!!.pagination
                        isLoaded = true
                        notifyPropertyChanged(BR.loaded)
                        it.resume(Unit)
                    } catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<GameList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.gamesSeries(seriesId, pagination.offset + pagination.size).enqueue(object : Callback<GameList> {

            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                try {
                    pagination = response.body()!!.pagination
                    adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                } catch (e: Exception) {
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                //TODO: do something
            }
        })
    }
}