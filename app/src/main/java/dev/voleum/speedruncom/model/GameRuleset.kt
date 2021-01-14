package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class GameRuleset(
    @SerializedName("show-milliseconds") val showMilliseconds: Boolean,
    @SerializedName("require-verification") val requireVerification: Boolean,
    @SerializedName("require-video") val requireVideo: Boolean,
    @SerializedName("run-times") val runTimes: List<String>,
    @SerializedName("default-time") val defaultTime: String,
    @SerializedName("emulators-allowed") val emulatorsAllowed: Boolean,
)