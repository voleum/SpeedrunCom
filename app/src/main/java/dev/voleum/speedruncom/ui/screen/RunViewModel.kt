package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.adapter.RunVideosRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.DataRun
import dev.voleum.speedruncom.model.Link
import dev.voleum.speedruncom.model.Run
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RunViewModel : AbstractViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<Link>) {
            if (recyclerView.adapter is RunVideosRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as RunVideosRecyclerViewAdapter
                if (adapter.items != list)
                    adapter.replaceItems(list)
            }
        }
    }

    lateinit var id: String

    var run: Run? = null
        @Bindable get
        @Bindable set

    var adapter = RunVideosRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data = adapter.items
        @Bindable get

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.runs(id).enqueue(object : Callback<DataRun> {
                override fun onResponse(call: Call<DataRun>, response: Response<DataRun>) {
                    try {
                        run = response.body()!!.data
                        adapter.replaceItems(run!!.videos.links)
                        isLoaded = true
                        notifyPropertyChanged(BR.loaded)
                        it.resume(Unit)
                    }
                    catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<DataRun>, t: Throwable) {
                    t.stackTrace
                    it.resumeWithException(t)
                }
            })
        }
    }
}