package dev.voleum.speedruncom.ui.nav.search

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.recyclerview.SearchRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.SeriesList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Any>) {
//            if (recyclerView.adapter is SearchRecyclerViewAdapter) {
//                val adapter = recyclerView.adapter as SearchRecyclerViewAdapter

//            }
        }
    }

    var searchString = ""

    var adapter = SearchRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    val data: List<Any>
        @Bindable get() = adapter.searchResult.getList()

    suspend fun findSeries() {
        suspendCoroutine<Unit> {
            API.series(searchString).enqueue(object : Callback<SeriesList> {
                override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                    try {
                        adapter.addSeries(response.body()!!.data)
                        it.resume(Unit)
                    } catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    it.resumeWithException(t)
                }
            })
        }
    }

    suspend fun findGames() {
        suspendCoroutine<Unit> {
            API.games(searchString).enqueue(object : Callback<GameList> {
                override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                    try {
                        adapter.addGames(response.body()!!.data)
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
}