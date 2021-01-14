package dev.voleum.speedruncom.api

import dev.voleum.speedruncom.model.*
import retrofit2.Call
import retrofit2.http.*

interface SpeedrunComApi {

    /**
     * This will retrieve a single category, identified by its ID.
     */
    @GET("categories/{id}")
    fun category(@Path("id") id: String): Call<Category>

    /**
     * This will retrieve all categories of a given game.
     * The id can be either the game ID or its abbreviation.
     */
    @GET("games/{game}/categories")
    fun categoriesGame(@Path("game") gameId: String): Call<CategoryList>

    /**
     * This will retrieve the applicable categories for the given level.
     */
    @GET("games/{level}/categories")
    fun categoriesLevel(@Path("level") levelId: String): Call<CategoryList>

    /**
     * This will return a list of all games.
     */
    @GET("games")
    fun games(): Call<GameList>

    /**
     *  This will return a list of all games using pagination.
     */
    @GET("games")
    fun games(@Query("offset") offset: Int): Call<GameList>

    /**
     *  This will return a list of all games by name.
     */
    @GET("games")
    fun games(@Query("name") name: String): Call<GameList>

    /**
     * This will retrieve a single game, identified by its ID.
     * Instead of the game's ID, you can also specify the game's abbreviation.
     * When an abbreviation was found, the API will respond with a redirect the the ID-based URL.
     */
    @GET("games/{id}")
    fun game(@Path("id") id: String): Call<DataGame>

    /**
     * This will retrieve a single game, identified by its ID with embedding related resources.
     * Instead of the game's ID, you can also specify the game's abbreviation.
     * When an abbreviation was found, the API will respond with a redirect the the ID-based URL.
     */
    @GET("games/{id}")
    fun gameEmbed(
        @Path("id") id: String,
        @Query("embed") embed: String,
    ): Call<DataGameEmbed>

//    @GET("guests")
//    fun guests(): Call<Guests>

    /**
     * This will retrieve all levels of a given game.
     * The id can be either the game ID or its abbreviation.
     */
    @GET("{game}/levels")
    fun levels(@Path("game") gameId: String): Call<LevelList>

    /**
     * This will return a full-game leaderboard.
     * The game and category can be either IDs or the respective abbreviations.
     */
    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategory(
        @Path("game") gameId: String,
        @Path("category") categoryId: String,
    ): Call<DataLeaderboard>

    /**
     * This will return a full-game leaderboard with embedding related resources by variable parameter.
     * The game and category can be either IDs or the respective abbreviations.
     */
    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategory(
        @Path("game") gameId: String,
        @Path("category") categoryId: String,
        @QueryMap variable: Map<String, String>?,
    ): Call<DataLeaderboard>

    /**
     * This will return a full-game leaderboard by variable parameter.
     * The game and category can be either IDs or the respective abbreviations.
     * The query string parameter named var-[variable ID here] for using the value ID as the value.
     */
    @GET("leaderboards/{game}/category/{category}")
    fun leaderboardsCategoryEmbed(
        @Path("game") gameId: String,
        @Path("category") categoryId: String,
        @QueryMap variable: Map<String, String>?,
        @Query("embed") embed: String,
    ): Call<DataLeaderboardEmbed>

    /**
     * This will retrieve the notifications for the currently authenticated user.
     */
    @GET("notifications")
    fun notifications(@Header("X-API-Key") key: String): Call<NotificationList>

    /**
     * This will retrieve the notifications for the currently authenticated user using pagination.
     */
    @GET("notifications")
    fun notifications(
        @Header("X-API-Key") key: String,
        @Query("offset") offset: Int,
    ): Call<NotificationList>

    /**
     * This will retrieve a single platform, identified by its ID.
     */
    @GET("platforms/{id}")
    fun platform(@Path("id") id: String): Call<DataPlatform>

    /**
     * This will retrieve the current user.
     * */
    @GET("profile")
    fun profile(@Header("X-API-Key") key: String): Call<DataUser>

    /**
     * This will retrieve a single region, identified by its ID.
     */
    @GET("regions/{id}")
    fun regions(@Path("id") id: String): Call<GameRegion>

    /**
     * This will return a list of all runs.
     */
    @GET("runs")
    fun runs(): Call<RunList>

    /**
     * This will return a sorted list of all runs.
     */
    @GET("runs")
    fun runsSorted(
        @Query("orderby") orderBy: String,
        @Query("direction") direction: String,
        @Query("embed") embed: String,
    ): Call<RunEmbedList>

    /**
     * This will return a sorted list of all runs using pagination.
     */
    @GET("runs")
    fun runsSorted(
        @Query("orderby") orderBy: String,
        @Query("direction") direction: String,
        @Query("embed") embed: String,
        @Query("offset") offset: Int,
    ): Call<RunEmbedList>

    /**
     * This will retrieve a single run, identified by its ID.
     */
    @GET("/runs/{id}")
    fun runs(@Path("id") id: String): Call<DataRun>

    /**
     * This will return a list of all series.
     */
    @GET("series")
    fun series(): Call<SeriesList>

    /**
     * This will return a list of all series by name.
     */
    @GET("series")
    fun series(@Query("name") name: String): Call<SeriesList>

    /**
     * This will return a list of all series using pagination.
     */
    @GET("series")
    fun series(@Query("offset") offset: Int): Call<SeriesList>

    /**
     * This will retrieve all games of a given series.
     * Еhe ID can be either the actual series ID or its abbreviation.
     */
    @GET("series/{id}/games")
    fun gamesSeries(@Path("id") id: String): Call<GameList>

    /**
     * This will retrieve all games of a given series using pagination.
     * Еhe ID can be either the actual series ID or its abbreviation.
     */
    @GET("series/{id}/games")
    fun gamesSeries(
        @Path("id") id: String,
        @Query("offset") offset: Int,
    ): Call<GameList>

    /**
     * This will return a list of users.
     */
    @GET("users")
    fun users(): Call<UserList>

    /**
     * This will retrieve a single user, identified by their ID.
     * Instead of the ID, the username can be used as well.
     * But this is only recommended for quick lookups, as usernames can change over time.
     */
    @GET("users/{id}")
    fun users(@Path("id") id: String): Call<DataUser>

    /**
     * This will retrieve a single variable, identified by its ID.
     */
    @GET("variables/{id}")
    fun variables(@Path("id") id: String): Call<Variable>

    /**
     * This will retrieve all variables that are applicable to the given category.
     */
    @GET("categories/{category}/variables")
    fun variablesCategory(@Path("category") categoryId: String): Call<VariableList>

//    @POST("runs")
//    fun postRun(run: ): Call<>
}