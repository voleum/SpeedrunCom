package dev.voleum.speedruncom.model

import java.io.Serializable

data class Series(val id: String,
                  val names: Names,
                  val abbreviation: String,
                  val weblink: String,
                  val moderators: Map<String, String>,
                  val created: String,
                  val assets: Assets,
                  val links: List<Link>) : Serializable