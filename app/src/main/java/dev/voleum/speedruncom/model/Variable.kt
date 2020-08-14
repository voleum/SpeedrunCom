package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class Variable(val id: String,
                    val name: String,
                    val category: String,
                    val scope: Scope,
                    val mandatory: Boolean,
                    @SerializedName("user-defined") val userDefined: Boolean,
                    val obsoletes: Boolean,
                    val values: Values,
                    @SerializedName("is-subcategory") val isSubcategory: Boolean,
                    val links: List<Link>)