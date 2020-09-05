package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.adapter.LeaderboardRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.DataLeaderboardEmbed
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.ui.AbstractViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LeaderboardViewModel : AbstractViewModel() {

    companion object {
        @JvmStatic
        @BindingAdapter("data")
        fun setData(recyclerView: RecyclerView, list: List<RunLeaderboard>) {}
    }

    lateinit var gameId: String
    lateinit var categoryId: String
    lateinit var variableId: String
    lateinit var subcategoryId: String
    lateinit var trophyAssets: Assets

    var adapter = LeaderboardRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<RunLeaderboard> = adapter.leaderboard?.runs ?: listOf()
        @Bindable get
        @Bindable set

    private fun getSubcategoryQueryMap(): Map<String, String>? =
        if (subcategoryId != "") mapOf(Pair("var-$variableId", subcategoryId))
        else mapOf()

    suspend fun load() {
        suspendCoroutine<Unit> {

            API.leaderboardsCategoryEmbed(
                gameId,
                categoryId,
                getSubcategoryQueryMap(),
                "game,players"
            ).enqueue(object : Callback<DataLeaderboardEmbed> {

                override fun onResponse(call: Call<DataLeaderboardEmbed>, response: Response<DataLeaderboardEmbed>) {
                    try {
                        adapter.leaderboard = response.body()!!.data
                        adapter.trophyAssets = trophyAssets
                        data = adapter.leaderboard!!.runs
                        isLoaded = true
                        notifyChange()
                        it.resume(Unit)
                    } catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<DataLeaderboardEmbed>, t: Throwable) {
                    t.stackTrace
                    it.resumeWithException(t)
                }
            })
        }
    }
}