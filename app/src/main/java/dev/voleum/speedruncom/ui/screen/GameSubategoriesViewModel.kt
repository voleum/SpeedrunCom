package dev.voleum.speedruncom.ui.screen

import android.util.Log
import androidx.databinding.Bindable
import dev.voleum.speedruncom.api.API
import dev.voleum.speedruncom.enum.States
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryValues
import dev.voleum.speedruncom.model.VariableList
import dev.voleum.speedruncom.ui.ViewModelObservable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameSubategoriesViewModel : ViewModelObservable() {

    lateinit var gameId: String
    lateinit var categoryId: String
    lateinit var trophyAssets: Assets

    lateinit var loadListener: () -> Unit

    var state = States.CREATED

    var subcategories: Map<String, CategoryValues> = mapOf()
        @Bindable get

    fun getTabsText(): List<String> = subcategories.map { it.value.label }

    fun getSubcategoriesIds(): List<String> = subcategories.map { it.key }

    fun setListener(loadListener: () -> Unit) {
        this.loadListener = loadListener
    }

    fun load() {
        API.variablesCategory(categoryId).enqueue(object : Callback<VariableList> {
            override fun onResponse(call: Call<VariableList>, response: Response<VariableList>) {
                subcategories = response.body()!!.data.filter { it.isSubcategory }[0].values.values
                notifyChange()
                //TODO: exception if game not founded
                state = States.LOADED
                Log.d("tag", "load onResponse()")
                loadListener()
            }

            override fun onFailure(call: Call<VariableList>, t: Throwable) {
                t.stackTrace
                t.message
                state = States.ERROR
                Log.d("tag", "load onError()")
                loadListener()
            }
        })
    }
}