package dev.voleum.speedruncom.model

import java.io.Serializable

data class Link(val rel: String,
                val uri: String) : Serializable