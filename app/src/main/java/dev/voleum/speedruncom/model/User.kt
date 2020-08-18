package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class User(val id: String,
                val names: Names,
                val weblink: String,
                @SerializedName("name-style") val nameStyle: NameStyle,
                val role: String,
                val singup: String,
                val location: Location,
                val twitch: Link,
                val hitbox: Link,
                val youtube: Link,
                val twitter: Link,
                val speedrunslive: Link,
                val links: List<Link>)