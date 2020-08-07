package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.DataRun
import dev.voleum.speedruncom.model.Run
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RunViewModel : ViewModelObservable() {

    lateinit var id: String

    lateinit var loadListener: () -> Unit

    var state = States.CREATED

    var run: Run? = null
        @Bindable get
        @Bindable set

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun load() {
        API.runs(id).enqueue(object : Callback<DataRun> {
            override fun onResponse(call: Call<DataRun>, response: Response<DataRun>) {
                run = response.body()!!.data
                notifyChange()
                //TODO: exception if game not founded
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<DataRun>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError()")
                loadListener()
            }
        })
    }
}