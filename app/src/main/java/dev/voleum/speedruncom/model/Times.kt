package dev.voleum.speedruncom.model

data class Times(val primary: String,
                 val primary_t: Double,
                 val realtime: String,
                 val realtime_t: Double,
                 val realtime_noloads: String,
                 val realtime_noloads_t: Double,
                 val ingame: String,
                 val ingame_t: Double)