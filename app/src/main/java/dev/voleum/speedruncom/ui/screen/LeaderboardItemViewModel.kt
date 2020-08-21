package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.model.User
import dev.voleum.speedruncom.ui.ViewModelObservable
import java.util.concurrent.TimeUnit

class LeaderboardItemViewModel(record: RunLeaderboard, val user: User?) : ViewModelObservable() {

    val placeValue = record.place

    var place: String =
        if (placeValue == 0) "-"
        else placeValue.toString()
        @Bindable get
        @Bindable set

    var player: String =
        if (record.run.players[0].rel == PlayerTypes.USER.type)
            user?.names?.international ?: record.run.players[0].id
        else record.run.players[0].name //TODO: multiply players
        @Bindable get
        @Bindable set

    var time: String =
        String.format(
            "%02d:%02d:%02d",
            TimeUnit.SECONDS.toHours(record.run.times.primary_t.toLong()),
            TimeUnit.SECONDS.toMinutes(record.run.times.primary_t.toLong()) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(record.run.times.primary_t.toLong())),
            TimeUnit.SECONDS.toSeconds(record.run.times.primary_t.toLong()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(record.run.times.primary_t.toLong()))
        ) //TODO: multiply times maybe
        @Bindable get
        @Bindable set

    var date: String = record.run.date
        @Bindable get
        @Bindable set
}