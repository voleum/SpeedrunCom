package dev.voleum.speedruncom.ui.nav.home

import androidx.databinding.Bindable
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.model.RunEmbed
import dev.voleum.speedruncom.ui.ViewModelObservable
import kotlin.math.roundToInt
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes
import kotlin.time.seconds

@ExperimentalTime
class RunsItemViewModel(val run: RunEmbed) : ViewModelObservable() {

    var game: String = run.game.data.names.international
        @Bindable get

    var category: String = run.category.data.name
        @Bindable get

    var player: String =
        //TODO multiply players
        if (run.players.data[0].rel == PlayerTypes.USER.value)
            run.players.data[0].names?.international ?: ""
        else run.players.data[0].name ?: ""
        @Bindable get

    var date: String = run.date
        @Bindable get

    var time: String =
        //TODO multiply times maybe
        String.format(
            "%02d:%02d:%02d",
            run.times.primaryT.seconds.inHours.toInt(),
            run.times.primaryT.seconds.inMinutes.toInt() -
                    run.times.primaryT.seconds.inHours.toInt().hours.inMinutes.toInt(),
            run.times.primaryT.toInt() -
                    run.times.primaryT.toInt().seconds.inMinutes.toInt().minutes.inSeconds.toInt()
        ) + addMilliseconds()
        @Bindable get

    private fun addMilliseconds() =
        if (run.game.data.ruleset.showMilliseconds)
            ".${((run.times.primaryT.seconds.inSeconds -
                    run.times.primaryT.seconds.inSeconds.toInt())
                    * 1000).roundToInt()}"
        else ""
}