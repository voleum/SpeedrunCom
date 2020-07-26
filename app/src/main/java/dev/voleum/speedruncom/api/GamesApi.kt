package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.GameList
import retrofit2.Call
import retrofit2.http.GET

interface GamesApi {

    @GET("/api/v1/games")
    fun games(): Call<GameList>
}