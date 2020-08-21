package dev.voleum.speedruncom.ui.screen

import android.view.View
import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Assets
import dev.voleum.speedruncom.model.CategoryEmbed
import dev.voleum.speedruncom.model.CategoryValues
import dev.voleum.speedruncom.model.Variable
import dev.voleum.speedruncom.ui.ViewModelObservable

class GameSubategoriesViewModel : ViewModelObservable() {

    lateinit var gameId: String
    lateinit var category: CategoryEmbed
    lateinit var trophyAssets: Assets

    private val subcategories: Map<String, CategoryValues>
        @Bindable get() =
            category.variables.data.find { it.isSubcategory } ?.values?.values ?: mapOf()

    private val subcategoriesVariable: Variable?
        get() = category.variables.data.find { it.isSubcategory }

    val variableId: String
        get() = subcategoriesVariable?.id ?: ""

    val tabsVisibility: Int
        @Bindable get() =
            if (subcategories.isEmpty()) View.GONE
            else View.VISIBLE

    val tabsText: List<String>
        get() = subcategories.map { it.value.label }

    val subcategoriesIds: List<String>
            get() =
                if (subcategories.isNotEmpty()) subcategories.map { it.key }
                else listOf("")
}