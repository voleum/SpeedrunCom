package dev.voleum.speedruncom.ui.nav.home

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.recyclerview.RunsRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.model.RunEmbed
import dev.voleum.speedruncom.model.RunEmbedList
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : AbstractViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<RunEmbed>) {
            if (recyclerView.adapter is RunsRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as RunsRecyclerViewAdapter
                if (adapter.items != list)
                    adapter.replaceItems(list)
            }
        }
    }

    lateinit var pagination: Pagination

    var adapter = RunsRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<RunEmbed> = adapter.items
        @Bindable get

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.runsSorted("date", "desc", "game,category,players").enqueue(object : Callback<RunEmbedList> {
                override fun onResponse(call: Call<RunEmbedList>, response: Response<RunEmbedList>) {
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

                override fun onFailure(call: Call<RunEmbedList>, t: Throwable) {
                    t.stackTrace
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.runsSorted("date", "desc", "game,category,players", pagination.offset + pagination.size).enqueue(object : Callback<RunEmbedList> {
            override fun onResponse(call: Call<RunEmbedList>, response: Response<RunEmbedList>) {
                try {
                    pagination = response.body()!!.pagination
                    adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                } catch (e: Exception) {
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<RunEmbedList>, t: Throwable) {
                t.stackTrace
                //TODO: do something
            }
        })
    }
}