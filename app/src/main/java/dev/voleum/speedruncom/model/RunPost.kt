package dev.voleum.speedruncom.model

data class RunPost(
    val category: String,
    val level: String,
    val date: String,
    val region: String,
    val platform: String,
    val verified: Boolean,
    val times: Times,
    val players: List<Player>,
    val emulated: Boolean,
    val video: String,
    val comment: String,
    val splitsio: String,
    val variables: Values,
)