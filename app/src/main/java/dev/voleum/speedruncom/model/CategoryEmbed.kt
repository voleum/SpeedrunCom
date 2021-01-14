package dev.voleum.speedruncom.model

import java.io.Serializable

data class CategoryEmbed(
    val id: String,
    val name: String,
    val weblink: String,
    val type: String,
    val rules: String,
    val players: CategoryPlayers,
    val miscellaneous: Boolean,
    val list: List<Link>,
    val variables: VariableList,
) : Serializable