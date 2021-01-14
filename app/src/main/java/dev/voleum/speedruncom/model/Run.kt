package dev.voleum.speedruncom.model

data class Run(
    val id: String,
    val weblink: String,
    val game: String,
    val level: String,
    val category: String,
    val videos: Videos,
    val comment: String,
    val status: Status,
    val players: List<Player>,
    val date: String,
    val submitted: String,
    val times: Times,
    val system: GameSystem,
    val splits: Link,
    val values: Map<String, String>,
    val links: List<Link>,
)