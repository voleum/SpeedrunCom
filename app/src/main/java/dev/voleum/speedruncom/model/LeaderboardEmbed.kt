package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class LeaderboardEmbed(val weblink: String,
                            val game: DataGame,
                            val category: String,
                            val level: String,
                            val platform: String,
                            val region: String,
                            val emulators: String,
                            @SerializedName("video-only") val videoOnly: Boolean,
                            val timing: String,
                            val runs: List<RunLeaderboard>,
                            val links: List<Link>,
                            val players: UserList)