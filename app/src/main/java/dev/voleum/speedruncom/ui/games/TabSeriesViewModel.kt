package dev.voleum.speedruncom.ui.games

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.SeriesAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.model.SeriesList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TabSeriesViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Series>) {
            if (recyclerView.adapter is SeriesAdapter)
                (recyclerView.adapter as SeriesAdapter).replaceItems(list)
        }
    }

    lateinit var loadListener: () -> Unit

    lateinit var pagination: Pagination

    var state = States.CREATED

    var adapter = SeriesAdapter()
        @Bindable get
        @Bindable set

    var data: List<Series> = adapter.items
        @Bindable get

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun onRefresh() {
        state = States.PROGRESS
        load()
    }

    fun load() {
        API.series().enqueue(object : Callback<SeriesList> {
            override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                data = response.body()!!.data
                pagination = response.body()!!.pagination
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError()")
                loadListener()
            }

        })
    }

    fun loadMore() {
        API.series(pagination.offset + pagination.size).enqueue(object : Callback<SeriesList> {
            override fun onResponse(call: Call<SeriesList>, response: Response<SeriesList>) {
                pagination = response.body()!!.pagination
                adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                state = States.LOADED
                Log.d("tag", "loadMore onResponse(); data.size: ${data.size}; adapter.items.size: ${adapter.items.size}")
//                loadListener()
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
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