package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class NameStyle(
    val style: String,
    val color: Color,
    @SerializedName("color-from") val colorFrom: Color,
    @SerializedName("color-to") val colorTo: Color,
)