package dev.voleum.speedruncom.model

data class Level(val id: String,
                 val name: String,
                 val weblink: String,
                 val rules: String,
                 val links: List<Link>)