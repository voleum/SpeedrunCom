package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.RunLeaderboard
import dev.voleum.speedruncom.ui.ViewModelObservable

class LeaderboardItemViewModel(record: RunLeaderboard) : ViewModelObservable() {

//    var imageWidth: Int = 0
//        @Bindable get
//        set(value) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value.toFloat(), )
//
//    var imageHeight: Int = 0
//        @Bindable get

    var place: String = record.place.toString()
        @Bindable get
        @Bindable set

    var player: String = if (record.run.players[0].rel == "user") record.run.players[0].id else record.run.players[0].name //TODO: multiply players
        @Bindable get
        @Bindable set

    var time: String = record.run.times.primary_t.toString() //TODO: multiply times maybe
        @Bindable get
        @Bindable set

    var date: String = record.run.date
        @Bindable get
        @Bindable set
}