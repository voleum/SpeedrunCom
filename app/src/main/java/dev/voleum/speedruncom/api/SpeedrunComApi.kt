package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SpeedrunComApi {

    @GET("categories/{id}")
    fun categories(@Path("id") id: String): Call<Category>

    @GET("games")
    fun games(): Call<GameList>

//    @GET("guests")
//    fun notifications(): Call<Guests>

//    @GET("levels")
//    fun notifications(): Call<Levels>

//    @GET("leaderboards")
//    fun notifications(): Call<Leaderboards>

    @GET("platform")
    fun platform(): Call<Platform>

//    @GET("profile")
//    fun profile(): Call<User>

    @GET("regions/{id}")
    fun regions(@Path("id") id: String): Call<GameRegion>

    @GET("runs")
    fun runs(): Call<RunList>

    @GET("series")
    fun series(): Call<SeriesList>

    @GET("users")
    fun users(): Call<UserList>

    @GET("variables/{id}")
    fun variables(@Path("id") id: String): Call<Variable>
}