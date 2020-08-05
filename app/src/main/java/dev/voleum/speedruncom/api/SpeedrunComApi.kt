package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpeedrunComApi {

    @GET("categories/{id}")
    fun category(@Path("id") id: String): Call<Category>

    @GET("games/{game}/categories")
    fun categories(@Path("game") gameId: String): Call<CategoryList>

    @GET("games")
    fun games(): Call<GameList>

    @GET("games")
    fun games(@Query("offset") offset: Int): Call<GameList>

    @GET("games/{id}")
    fun games(@Path("id") id: String): Call<DataGame>

//    @GET("guests")
//    fun guests(): Call<Guests>

//    @GET("levels")
//    fun levels(): Call<Levels>

    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategory(@Path("game") gameId: String,
                             @Path("category") categoryId: String): Call<LeaderboardList>

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

    @GET("series")
    fun series(@Query("offset") offset: Int): Call<SeriesList>

    @GET("users")
    fun users(): Call<UserList>

    @GET("variables/{id}")
    fun variables(@Path("id") id: String): Call<Variable>
}