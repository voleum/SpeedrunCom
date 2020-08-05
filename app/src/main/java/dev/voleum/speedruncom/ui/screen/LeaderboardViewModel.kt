package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.LeaderboardRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.LeaderboardList
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardViewModel : ViewModelObservable() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<RunLeaderboard>) {
            if (recyclerView.adapter is LeaderboardRecyclerViewAdapter) {
                val adapter = recyclerView.adapter as LeaderboardRecyclerViewAdapter
                if (adapter.items != list)
                    adapter.replaceItems(list)
            }
        }
    }

    lateinit var gameId: String
    lateinit var categoryId: String
    lateinit var trophyAssets: Assets

    lateinit var loadListener: () -> Unit

    var state = States.CREATED

    var adapter = LeaderboardRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<RunLeaderboard> = adapter.items
        @Bindable get

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun load() {
        API.leaderboardsCategory(gameId, categoryId).enqueue(object : Callback<LeaderboardList> {
            override fun onResponse(call: Call<LeaderboardList>, response: Response<LeaderboardList>) {
                adapter.replaceItems(response.body()!!.data.runs)
                adapter.trophyAssets = trophyAssets
//                pagination = response.body()!!.pagination
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<LeaderboardList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError(): ${t.message}")
                loadListener()
            }
        })
    }
}