package dev.voleum.speedruncom.model

data class RunEmbed(val id: String,
                    val weblink: String,
                    val game: DataGame,
                    val level: String,
                    val category: DataCategory,
                    val videos: Videos,
                    val comment: String,
                    val status: Status,
                    val players: PlayerUserGuestList,
                    val date: String,
                    val submitted: String,
                    val times: Times,
                    val system: GameSystem,
                    val splits: Link,
                    val values: Map<String, String>,
                    val links: List<Link>)