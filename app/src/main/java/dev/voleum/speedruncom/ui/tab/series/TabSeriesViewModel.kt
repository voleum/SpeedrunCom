package dev.voleum.speedruncom.ui.tab.series

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.SeriesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.model.SeriesList
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TabSeriesViewModel : AbstractViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Series>) {
            if (recyclerView.adapter is SeriesRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as SeriesRecyclerViewAdapter
                if (adapter.items != list)
                    adapter.replaceItems(list)
            }
        }
    }

    lateinit var pagination: Pagination

    var adapter = SeriesRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Series> = adapter.items
        @Bindable get

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.series().enqueue(object : Callback<SeriesList> {
                override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
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

                override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.series(pagination.offset + pagination.size).enqueue(object : Callback<SeriesList> {
            override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                try {
                    pagination = response.body()!!.pagination
                    adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                } catch (e: Exception) {
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                t.stackTrace
                t.message
                //TODO: do something
            }
        })
    }
}