package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class Times(
    val primary: String,
    @SerializedName("primary_t") val primaryT: Double,
    val realtime: String,
    @SerializedName("realtime_t") val realtimeT: Double,
    @SerializedName("realtime_noloads") val realtimeNoloads: String,
    @SerializedName("realtime_noloads_t") val realtimeNoloadsT: Double,
    val ingame: String,
    @SerializedName("ingame_t") val ingameT: Double,
)