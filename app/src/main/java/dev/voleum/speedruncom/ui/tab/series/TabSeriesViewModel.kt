package dev.voleum.speedruncom.ui.tab.series

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.SeriesRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
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

//    lateinit var loadListener: () -> Unit

    lateinit var pagination: Pagination

//    var state = States.CREATED

    var adapter = SeriesRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Series> = adapter.items
        @Bindable get

//    fun setListener(loadListener: () -> Unit) {
//        this.loadListener = loadListener
//    }

    suspend fun onRefresh() {
//        state = States.PROGRESS
        load()
    }

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.series().enqueue(object : Callback<SeriesList> {
                override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                    adapter.replaceItems(response.body()!!.data)
                    pagination = response.body()!!.pagination
//                state = States.LOADED
                    Log.d("tag", "load onResponse()")
//                loadListener()
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                    t.stackTrace
                    t.message
//                state = States.ERROR
                    Log.d("tag", "load onError()")
//                loadListener()
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
//                state = States.LOADED
                Log.d("tag", "loadMore onResponse(); data.size: ${data.size}; adapter.items.size: ${adapter.items.size}")
//                loadListener()
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                t.stackTrace
                t.message
//                state = States.ERROR
                Log.d("tag", "loadMore onError()")
                //TODO: do something
//                loadListener()
            }
        })
    }
}