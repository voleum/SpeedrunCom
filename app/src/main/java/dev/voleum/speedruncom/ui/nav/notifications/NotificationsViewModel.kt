package dev.voleum.speedruncom.ui.nav.notifications

import android.content.SharedPreferences
import androidx.databinding.Bindable
import dev.voleum.speedruncom.API_KEY_ENCRYPTED_PREF_NAME
import dev.voleum.speedruncom.adapter.NotificationsRecyclerViewAdapter
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.decrypt
import dev.voleum.speedruncom.model.Notification
import dev.voleum.speedruncom.model.NotificationList
import dev.voleum.speedruncom.model.Pagination
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NotificationsViewModel : ViewModelObservable() {

    lateinit var apiKeyEncrypted: String

    var isLoaded = false

    lateinit var pagination: Pagination

    var adapter = NotificationsRecyclerViewAdapter()
        @Bindable get
        @Bindable set

    var data: List<Notification> = adapter.items
        @Bindable get
        @Bindable set

    fun loadApiKey(sharedPreferences: SharedPreferences) {
        apiKeyEncrypted = sharedPreferences.getString(API_KEY_ENCRYPTED_PREF_NAME, "") ?: ""
    }

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.notifications(String(decrypt(apiKeyEncrypted))).enqueue(object : Callback<NotificationList> {

                override fun onResponse(
                    call: Call<NotificationList>,
                    response: Response<NotificationList>
                ) {
                    try {
                        adapter.replaceItems(response.body()!!.data)
                        pagination = response.body()!!.pagination
                        isLoaded = true
                        it.resume(Unit)
                    }
                    catch (e: Exception) {
                        onFailure(call, e)
                    }
                }

                override fun onFailure(call: Call<NotificationList>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }

    fun loadMore() {
        API.notifications(String(decrypt(apiKeyEncrypted)),
            pagination.offset + pagination.size).enqueue(object : Callback<NotificationList> {

            override fun onResponse(
                call: Call<NotificationList>,
                response: Response<NotificationList>
            ) {
                try {
                    pagination = response.body()!!.pagination
                    adapter.addItems(response.body()!!.data, pagination.offset, pagination.size)
                }
                catch (e: Exception) {
                    onFailure(call, e)
                }
            }

            override fun onFailure(call: Call<NotificationList>, t: Throwable) {
                t.stackTrace
                t.message
                //TODO: do something
            }
        })
    }
}