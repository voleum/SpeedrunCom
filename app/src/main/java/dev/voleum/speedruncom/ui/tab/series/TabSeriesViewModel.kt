package dev.voleum.speedruncom.ui.tab.series

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.SeriesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.model.SeriesList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TabSeriesViewModel : ViewModelObservable() {

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

    var isSeriesLoaded = false
        @Bindable get

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
                    adapter.replaceItems(response.body()!!.data)
                    pagination = response.body()!!.pagination
                    Log.d("tag", "load onResponse()")
                    isSeriesLoaded = true
                    notifyPropertyChanged(BR.seriesLoaded)
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    Log.d("tag", "load onError()")
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.series(pagination.offset + pagination.size).enqueue(object : Callback<SeriesList> {
            override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                pagination = response.body()!!.pagination
                adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                Log.d("tag", "loadMore onResponse(); data.size: ${data.size}; adapter.items.size: ${adapter.items.size}")
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                t.stackTrace
                t.message
                Log.d("tag", "loadMore onError()")
                //TODO: do something
            }
        })
    }
}