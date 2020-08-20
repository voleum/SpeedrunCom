package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.enum.PlayerTypes
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.model.User
import dev.voleum.speedruncom.ui.ViewModelObservable
import java.util.concurrent.TimeUnit

class LeaderboardItemViewModel(record: RunLeaderboard, val user: User?) : ViewModelObservable() {

    var place: String = record.place.toString()
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
            TimeUnit.MILLISECONDS.toHours(record.run.times.primary_t.toLong()*1000),
            TimeUnit.MILLISECONDS.toMinutes(record.run.times.primary_t.toLong()*1000) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(record.run.times.primary_t.toLong()*1000)),
            TimeUnit.MILLISECONDS.toSeconds(record.run.times.primary_t.toLong()*1000) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(record.run.times.primary_t.toLong()*1000))
        ) //TODO: multiply times maybe
        @Bindable get
        @Bindable set

    var date: String = record.run.date
        @Bindable get
        @Bindable set
}