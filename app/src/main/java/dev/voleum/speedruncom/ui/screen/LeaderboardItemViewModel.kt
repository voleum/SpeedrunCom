package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.DataUser
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.model.User
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LeaderboardItemViewModel(val record: RunLeaderboard) : ViewModelObservable() {

    var user: User? = null

    var place: String = record.place.toString()
        @Bindable get
        @Bindable set

    var player: String = ""
        @Bindable get() =
            if (record.run.players[0].rel == "user")
                if (user != null) user?.names!!.international
                else record.run.players[0].id
            else record.run.players[0].name //TODO: multiply players
        @Bindable set

    var time: String =
        String.format(
            "%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(record.run.times.primary_t.toLong()*1000),
            TimeUnit.MILLISECONDS.toMinutes(record.run.times.primary_t.toLong()*1000) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(record.run.times.primary_t.toLong()*1000)),
            TimeUnit.MILLISECONDS.toSeconds(record.run.times.primary_t.toLong()*1000) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(record.run.times.primary_t.toLong()*1000))
        ) //TODO: multiply times maybe
        @Bindable get
        @Bindable set

    var date: String = record.run.date
        @Bindable get
        @Bindable set

    suspend fun loadUser(id: String) {
        suspendCoroutine<Unit> {
            API.users(id).enqueue(object : Callback<DataUser> {

                override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                    user = response.body()!!.data
                    notifyPropertyChanged(BR.player)
                    Log.d("tag", "onResponse, id: $id, name: ${user!!.names.international}")
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