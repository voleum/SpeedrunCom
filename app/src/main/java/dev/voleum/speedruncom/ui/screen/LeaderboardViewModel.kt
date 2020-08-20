package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.LeaderboardRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.DataUser
import dev.voleum.speedruncom.model.LeaderboardList
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.ui.ViewModelObservable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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
    lateinit var variableId: String
    lateinit var subcategoryId: String
    lateinit var trophyAssets: Assets

    var isLeaderboardLoaded = false
    var isUsersLoaded = false

    var adapter = LeaderboardRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<RunLeaderboard> = adapter.items
        @Bindable get

    private fun getSubcategoryQueryMap(): Map<String, String>? =
        if (subcategoryId != "") mapOf(Pair("var-$variableId", subcategoryId))
        else mapOf()

    suspend fun loadUsers() {
        suspendCoroutine<Unit> {
            GlobalScope.launch {
                val jobs = mutableListOf<Job>()
                    data.forEach {
                        if (it.run.players[0].rel == PlayerTypes.USER.type) {
                            val job = launch { loadUser(it.run.players[0].id) }
                            jobs.add(job)
                        }
                    }
                jobs.forEach { it.join() }
                isUsersLoaded = true
                notifyChange()
            }
            it.resume(Unit)
        }
    }

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.leaderboardsCategory(gameId, categoryId, getSubcategoryQueryMap()).enqueue(object : Callback<LeaderboardList> {

                override fun onResponse(call: Call<LeaderboardList>, response: Response<LeaderboardList>) {
                    adapter.replaceItems(response.body()!!.data.runs)
                    adapter.trophyAssets = trophyAssets
                    Log.d("tag", "load onResponse()")
                    isLeaderboardLoaded = true
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<LeaderboardList>, t: Throwable) {
                    t.stackTrace
                    t.message
                    Log.d("tag", "load onError(): ${t.message}")
                    it.resumeWithException(t)
                }
            })
        }
    }

    suspend fun loadUser(id: String) {
        suspendCoroutine<Unit> {
            API.users(id).enqueue(object : Callback<DataUser> {

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    adapter.users[id] = response.body()!!.data
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<DataUser>, t: Throwable) {
                    t.stackTrace
                    t.message
                    Log.d("tag", "onFailure, id: $id")
                    it.resumeWithException(t)
                }
            })
        }
    }
}