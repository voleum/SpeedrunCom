package dev.voleum.speedruncom.model

import java.io.Serializable

data class CategoryPlayers(
    val type: String,
    val value: Int,
) : Serializable