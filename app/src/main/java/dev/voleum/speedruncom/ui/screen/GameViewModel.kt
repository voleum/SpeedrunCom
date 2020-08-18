package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.BR
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.*
import dev.voleum.speedruncom.ui.ViewModelObservable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GameViewModel : ViewModelObservable() {

    lateinit var id: String
    val platformList: MutableList<Platform> = mutableListOf()

    var isInfoLoaded = false
    var isCategoriesLoaded = false
    var isPlatformsLoaded = false

//    lateinit var loadInfoListener: () -> Unit
//    lateinit var loadCategoriesListener: () -> Unit

//    var stateInfo = States.CREATED
//    var stateCategories = States.CREATED

    val backgroundUrl: String
        get() = game?.assets?.background?.uri ?: ""

    val trophyAssets: Assets
        get() = game!!.assets

    var game: Game? = null
        @Bindable get
        @Bindable set

    var name: String = ""
        @Bindable get() = game?.names?.international ?: ""
        @Bindable set

    var releaseDate: String = ""
        @Bindable get() = game?.releaseDate ?: ""
        @Bindable set

    var platforms: String = ""
        @Bindable get() {
            var platforms = ""
            platformList.forEach { platforms += "${it.name}, " }
            return platforms.removeSuffix(", ")
        } //{
        //            return if (game != null) {
//                var platforms = ""
//                game?.platforms?.forEach { platforms += "$it, " }
//                platforms.removeSuffix(", ")
//            } else ""
//        }
        @Bindable set

    var categories: List<Category> = mutableListOf()
        @Bindable get
        @Bindable set

//    fun setInfoListener(loadListener: () -> Unit) {
//        this.loadInfoListener = loadListener
//    }
//
//    fun setCategoriesListener(loadListener: () -> Unit) {
//        this.loadCategoriesListener = loadListener
//    }

    suspend fun loadPlatforms() {
        suspendCoroutine<Unit> {
            GlobalScope.launch {
                val job = async {
                    game?.platforms?.forEach { launch { loadPlatform(it) } }
                }
                job.await()
                notifyPropertyChanged(BR.platforms)
                isPlatformsLoaded = true
                it.resume(Unit)
            }
        }
    }

    suspend fun loadInfo() {
        suspendCoroutine<Unit> {
            API.games(id).enqueue(object : Callback<DataGame> {
                override fun onResponse(call: Call<DataGame>, response: Response<DataGame>) {
                    game = response.body()!!.data
                    notifyChange()
                    //TODO: exception if game not founded
//                stateInfo = States.LOADED
                    Log.d("tag", "load onResponse()")
//                loadInfoListener()
                    isInfoLoaded = true
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<DataGame>, t: Throwable) {
                    t.stackTrace
                    t.message
//                stateInfo = States.ERROR
                    Log.d("tag", "load onError()")
//                loadInfoListener()
                    it.resumeWithException(t)
                }
            })
        }
    }

    suspend fun loadCategories() {
        suspendCoroutine<Unit> {
            API.categoriesGame(id).enqueue(object : Callback<CategoryList> {
                override fun onResponse(
                    call: Call<CategoryList>,
                    response: Response<CategoryList>
                ) {
                    categories =
                        response.body()!!.data.filter { it.type == "per-game" } //TODO add for levels
                    notifyChange()
                    //TODO exception if game not founded
//                stateCategories = States.LOADED
                    Log.d("tag", "load onResponse()")
//                loadCategoriesListener()
                    isCategoriesLoaded = true
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                    t.stackTrace
                    t.message
//                stateCategories = States.ERROR
                    Log.d("tag", "load onError()")
//                loadCategoriesListener()
                    it.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun loadPlatform(id: String) {
        suspendCoroutine<Unit> {
            API.platform(id).enqueue(object : Callback<DataPlatform> {

                override fun onResponse(
                    call: Call<DataPlatform>,
                    response: Response<DataPlatform>
                ) {
                    platformList.add(response.body()!!.data)
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<DataPlatform>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }
}