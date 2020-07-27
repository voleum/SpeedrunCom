package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class User(val id: String,
                val names: Names,
                val weblink: String,
                @SerializedName("name-style") val nameStyle: NameStyle,
                val role: String,
                val singup: String,
                val location: Location,
                val twitch: String,
                val hitbox: String,
                val youtube: String,
                val twitter: String,
                val speedrunslive: String,
                val links: List<Link>)