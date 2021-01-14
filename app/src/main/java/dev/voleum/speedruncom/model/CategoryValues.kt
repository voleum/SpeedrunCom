package dev.voleum.speedruncom.model

import java.io.Serializable

data class CategoryValues(
    val label: String,
    val rules: String,
    val flags: Flags,
) : Serializable