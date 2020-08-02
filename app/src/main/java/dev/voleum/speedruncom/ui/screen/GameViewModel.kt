package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Category
import dev.voleum.speedruncom.model.CategoryList
import dev.voleum.speedruncom.model.DataGame
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameViewModel : ViewModelObservable() {

    lateinit var id: String

    lateinit var loadInfoListener: () -> Unit
    lateinit var loadCategoriesListener: () -> Unit

    var stateInfo = States.CREATED
    var stateCategories = States.CREATED

    var backgroundUrl: String = ""
        get() = game?.assets?.background?.uri ?: ""

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
            return if (game != null) {
                var platforms = ""
                game?.platforms?.forEach { platforms += "$it, " }
                platforms.removeSuffix(", ")
            } else ""
        }
        @Bindable set

    var categories: List<Category> = mutableListOf()
        @Bindable get
        @Bindable set

    fun setInfoListener(loadListener: () -> Unit) {
        this.loadInfoListener = loadListener
    }

    fun setCategoriesListener(loadListener: () -> Unit) {
        this.loadCategoriesListener = loadListener
    }

    fun loadInfo() {
        API.games(id).enqueue(object : Callback<DataGame> {
            override fun onResponse(call: Call<DataGame>, response: Response<DataGame>) {
                game = response.body()!!.data
                notifyChange()
                //TODO: exception if game not founded
                stateInfo = States.LOADED
                Log.d("tag", "load onResponse()")
                loadInfoListener()
            }

            override fun onFailure(call: Call<DataGame>, t: Throwable) {
                t.stackTrace
                t.message
                stateInfo = States.ERROR
                Log.d("tag", "load onError()")
                loadInfoListener()
            }
        })
    }

    fun loadCategories() {
        API.categories(id).enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                categories = response.body()!!.data
                notifyChange()
                //TODO: exception if game not founded
                stateCategories = States.LOADED
                Log.d("tag", "load onResponse()")
                loadCategoriesListener()
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                t.stackTrace
                t.message
                stateCategories = States.ERROR
                Log.d("tag", "load onError()")
                loadCategoriesListener()
            }
        })
    }
}