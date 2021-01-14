package dev.voleum.speedruncom.model

import java.io.Serializable

data class Values(
    val values: Map<String, CategoryValues>,
    val default: String,
) : Serializable