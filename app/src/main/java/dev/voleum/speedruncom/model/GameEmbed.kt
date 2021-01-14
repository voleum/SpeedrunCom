package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GameEmbed(
    val id: String,
    val names: GameNames,
    val abbreviation: String,
    val weblink: String,
    val released: Int,
    @SerializedName("release-date") val releaseDate: String,
    val ruleset: GameRuleset,
    val romhack: Boolean,
    val gametypes: List<String>,
    val platforms: PlatformList,
    val regions: List<String>,
    val genres: List<String>,
    val engines: List<String>,
    val developers: List<String>,
    val publishers: List<String>,
    val moderators: Map<String, String>,
    val created: String,
    val assets: Assets,
    val links: List<Link>,
    val categories: CategoryListEmbed,
) : Serializable