package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class Assets(val logo: Asset,
                  @SerializedName("cover-tiny") val coverTiny: Asset,
                  @SerializedName("cover-small") val coverSmall: Asset,
                  @SerializedName("cover-medium") val coverMedium: Asset,
                  @SerializedName("cover-large") val coverLarge: Asset,
                  val icon: Asset,
                  @SerializedName("trophy-1st") val trophyFirst: Asset,
                  @SerializedName("trophy-2nd") val trophySecond: Asset,
                  @SerializedName("trophy-3rd") val trophyThird: Asset,
                  @SerializedName("trophy-4th") val trophyForth: Asset,
                  val background: Asset,
                  val foreground: Asset)