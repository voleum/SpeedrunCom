package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import dev.voleum.speedruncom.adapter.GamesAdapter
import dev.voleum.speedruncom.api.api
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.model.GameList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GamesViewModel : ViewModel() {

    val adapter = GamesAdapter()
        @Bindable get

    lateinit var loadListener: () -> Unit

    lateinit var data: List<Game>
        @Bindable get
//    private set

    var state = States.CREATED

//    private val _text = MutableLiveData<String>().apply {
//        value = "This is games Fragment"
//    }
//    val text: LiveData<String> = _text

    fun load() {
        api.games().enqueue(object : Callback<GameList> {
            override fun onResponse(call: Call<GameList>, response: Response<GameList>) {
                data = response.body()!!.data
                state = States.LOADED
                loadListener()
            }

            override fun onFailure(call: Call<GameList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                loadListener()
            }

        })
    }

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }
}