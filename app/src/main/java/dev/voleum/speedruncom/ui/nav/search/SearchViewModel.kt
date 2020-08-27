package dev.voleum.speedruncom.ui.nav.search

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.SearchRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.Pagination
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
            if (recyclerView.adapter is SearchRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as SearchRecyclerViewAdapter
                if (adapter.searchResult != list)
                    adapter.replaceItems(list)
            }
        }
    }

    var searchString = ""

    var isGamesFound = false
        @Bindable get

    lateinit var seriesId: String

    lateinit var pagination: Pagination

    var adapter = SearchRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Any> = adapter.searchResult
        @Bindable get

    suspend fun findGames() {
        suspendCoroutine<Unit> {
            API.games(searchString).enqueue(object : Callback<GameList> {
                override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                    try {
                        adapter.replaceItems(response.body()!!.data)
//                        pagination = response.body()!!.pagination
                        isGamesFound = true
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

//    fun loadMore() {
//        API.gamesSeries(seriesId, pagination.offset + pagination.size).enqueue(object : Callback<GameList> {
//
//            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
//                try {
//                    pagination = response.body()!!.pagination
//                    adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
//                } catch (e: Exception) {
//                    onFailure(call, e)
//                }
//            }
//
//            override fun onFailure(call: Call<GameList>, t: Throwable) {
//                t.stackTrace
//                t.message
//                //TODO: do something
//            }
//        })
//    }
}