package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class Leaderboard(
    val weblink: String,
    val game: String,
    val category: String,
    val level: String, //TODO: check type
    val platform: String,
    val region: String,
    val emulators: String,
    @SerializedName("video-only") val videoOnly: Boolean,
    val timing: String,
    val runs: List<RunLeaderboard>,
    val links: List<Link>,
)