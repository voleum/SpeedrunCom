package dev.voleum.speedruncom.model

const val TYPE_SERIES = 1
const val TYPE_GAME = 2

class SearchResult {

    val series = mutableListOf<Series>()
    val games = mutableListOf<Game>()

    val size: Int
        get() = series.size + games.size

    operator fun get(position: Int): Any = getList()[position]
//        val map = indexToObject()
//        val pair = map.keys.find { position in it }
//        return map[pair]!!
//    }

    fun getList(): List<Any> {
        val list = mutableListOf<Any>()
        list.addAll(series)
        list.addAll(games)
        return list
    }

    fun getType(position: Int): Int {
        val map = indexToType()
        val pair = map.keys.find { position in it }
        return map[pair]!!
    }

    private fun indexToType(): Map<IntRange, Int> {
        var next: Int
        val series = Pair(0 until this.series.size, TYPE_SERIES)
        next = series.first.last + 1
        val games = Pair(next until next + this.games.size, TYPE_GAME)
        return mapOf(series, games)
    }

//    private fun indexToObject(): Map<IntRange, Any> {
//        val series = Pair(0 until this.series.size, Series::class.java)
//        val games = Pair(series.first.last until this.games.size, Game::class.java)
//        return mapOf(series, games)
//    }
}