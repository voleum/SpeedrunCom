package dev.voleum.speedruncom.model

data class Times(val primary: String,
                 val primary_t: Int,
                 val realtime: String,
                 val realtime_t: Int,
                 val realtime_noloads: String,
                 val realtime_noloads_t: Int,
                 val ingame: String,
                 val ingame_t: Int)