package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.model.GameRuleset
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.model.User
import dev.voleum.speedruncom.ui.ViewModelObservable
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds

@ExperimentalTime
class LeaderboardItemViewModel(
    val ruleset: GameRuleset,
    val record: RunLeaderboard,
    val user: User?
) : ViewModelObservable() {

    val placeValue = record.place

    var place: String =
        if (placeValue == 0) "-"
        else placeValue.toString()
        @Bindable get
        @Bindable set

    var player: String =
        //TODO multiply players
        if (record.run.players[0].rel == PlayerTypes.USER.value)
            user?.names?.international ?: record.run.players[0].id
        else record.run.players[0].name
        @Bindable get
        @Bindable set

    var date: String = record.run.date
        @Bindable get
        @Bindable set

    var time: String =
        //TODO multiply times maybe
        String.format(
            "%02d:%02d:%02d",
            record.run.times.primaryT.seconds.inHours.toInt(),
            record.run.times.primaryT.seconds.inMinutes.toInt() -
                    record.run.times.primaryT.seconds.inHours.toInt().hours.inMinutes.toInt(),
            record.run.times.primaryT.toInt() -
                    record.run.times.primaryT.toInt().seconds.inMinutes.toInt().minutes.inSeconds.toInt()
        ) + addMilliseconds()
        @Bindable get
        @Bindable set

    private fun addMilliseconds() =
        if (ruleset.showMilliseconds)
            ".${((record.run.times.primaryT.seconds.inSeconds -
                        record.run.times.primaryT.seconds.inSeconds.toInt())
                            * 1000).roundToInt()}"
        else ""
}