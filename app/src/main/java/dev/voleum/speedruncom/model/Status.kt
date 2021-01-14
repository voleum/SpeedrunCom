package dev.voleum.speedruncom.model

import com.google.gson.annotations.SerializedName

data class Status(
    val status: String,
    val examiner: String,
    @SerializedName("verify-data") val verifyDate: String,
)