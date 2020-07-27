package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.GameList
import dev.voleum.speedruncom.model.SeriesList
import retrofit2.Call
import retrofit2.http.GET

interface SpeedrunComApi {

    @GET("/api/v1/games")
    fun games(): Call<GameList>

    @GET("/api/v1/series")
    fun series(): Call<SeriesList>
}