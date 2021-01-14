package dev.voleum.speedruncom.model

import java.io.Serializable

data class Asset(
    val uri: String,
    val width: Int,
    val height: Int,
) : Serializable