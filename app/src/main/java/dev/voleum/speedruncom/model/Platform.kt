package dev.voleum.speedruncom.model

data class Platform(val id: String,
                    val name: String,
                    val released: Int,
                    val links: List<Link>)