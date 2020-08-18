package dev.voleum.speedruncom.ui.screen

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryValues
import dev.voleum.speedruncom.model.VariableList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class GameSubategoriesViewModel : ViewModelObservable() {

    lateinit var gameId: String
    lateinit var categoryId: String
    lateinit var trophyAssets: Assets

    var isSubcategoriesLoaded = false

//    lateinit var loadListener: () -> Unit

//    var state = States.CREATED

    var subcategories: Map<String, CategoryValues> = mapOf()
        @Bindable get

    var variableId: String = ""

    var tabsVisibility: Int = View.GONE
        @Bindable get() =
            if (subcategories.isEmpty()) View.GONE
            else View.VISIBLE

    fun getTabsText(): List<String> = subcategories.map { it.value.label }

    fun getSubcategoriesIds(): List<String> =
        if (subcategories.isNotEmpty()) subcategories.map { it.key }
        else listOf("")

//    fun setListener(loadListener: () -> Unit) {
//        this.loadListener = loadListener
//    }

    suspend fun load() {
        suspendCoroutine<Unit> {
            API.variablesCategory(categoryId).enqueue(object : Callback<VariableList> {
                override fun onResponse(call: Call<VariableList>, response: Response<VariableList>) {
                    if (response.body() == null) onFailure(call, NullPointerException())
                    val subcategoriesResponse = response.body()!!.data.filter { it.isSubcategory }
                    if (subcategoriesResponse.isNotEmpty()) {
                        subcategories = subcategoriesResponse[0].values.values
                        variableId = subcategoriesResponse[0].id
                    }
                    notifyChange()
                    //TODO exception if game not founded
//                    state = States.LOADED
                    Log.d("tag", "load onResponse()")
//                    loadListener()
                    isSubcategoriesLoaded = true
                    it.resume(Unit)
                }

                override fun onFailure(call: Call<VariableList>, t: Throwable) {
                    t.stackTrace
                    t.message
//                    state = States.ERROR
                    Log.d("tag", "load onError()")
//                    loadListener()
                    it.resumeWithException(t)
                }
            })
        }
    }
}