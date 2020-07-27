package dev.voleum.speedruncom.model

data class Category(val id: String,
                    val name: String,
                    val weblink: String,
                    val type: String,
                    val rules: String,
                    val players: CategoryPlayers,
                    val miscellaneous: Boolean,
                    val list: List<Link>)