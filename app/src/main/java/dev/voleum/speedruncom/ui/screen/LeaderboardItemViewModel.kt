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
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LeaderboardItemViewModel(val record: RunLeaderboard) : ViewModelObservable() {

//    var imageWidth: Int = 0
//        @Bindable get
//        set(value) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value.toFloat(), )
//
//    var imageHeight: Int = 0
//        @Bindable get

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

    var time: String = record.run.times.primary_t.toString() //TODO: multiply times maybe
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
//                    notifyChange()
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