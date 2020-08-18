package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface SpeedrunComApi {

    @GET("categories/{id}")
    fun category(@Path("id") id: String): Call<Category>

    @GET("games/{game}/categories")
    fun categoriesGame(@Path("game") gameId: String): Call<CategoryList>

    @GET("games/{level}/categories")
    fun categoriesLevel(@Path("level") levelId: String): Call<CategoryList>

    @GET("games")
    fun games(): Call<GameList>

    @GET("games")
    fun games(@Query("offset") offset: Int): Call<GameList>

    @GET("games/{id}")
    fun games(@Path("id") id: String): Call<DataGame>

//    @GET("guests")
//    fun guests(): Call<Guests>

    @GET("{game}/levels")
    fun levels(@Path("game") gameId: String): Call<LevelList>

    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategory(@Path("game") gameId: String,
                             @Path("category") categoryId: String): Call<LeaderboardList>

    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategory(@Path("game") gameId: String,
                             @Path("category") categoryId: String,
                             @QueryMap variable: Map<String, String>?): Call<LeaderboardList>

    @GET("platforms/{id}")
    fun platform(@Path("id") id: String): Call<DataPlatform>

//    @GET("profile")
//    fun profile(): Call<User>

    @GET("regions/{id}")
    fun regions(@Path("id") id: String): Call<GameRegion>

    @GET("runs")
    fun runs(): Call<RunList>

    @GET("runs/{id}")
    fun runs(@Path("id") id: String): Call<DataRun>

    @GET("series")
    fun series(): Call<SeriesList>

    @GET("series/{id}/games")
    fun gamesSeries(@Path("id") id: String): Call<GameList>

    @GET("series/{id}/games")
    fun gamesSeries(@Path("id") id: String, @Query("offset") offset: Int): Call<GameList>

    @GET("series")
    fun series(@Query("offset") offset: Int): Call<SeriesList>

    @GET("users")
    fun users(): Call<UserList>

    @GET("users/{id}")
    fun users(@Path("id") id: String): Call<DataUser>

    @GET("variables/{id}")
    fun variables(@Path("id") id: String): Call<Variable>

    @GET("categories/{category}/variables")
    fun variablesCategory(@Path("category") categoryId: String): Call<VariableList>
}