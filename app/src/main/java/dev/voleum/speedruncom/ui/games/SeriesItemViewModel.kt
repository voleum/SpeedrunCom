package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.ui.ViewModelObservable

class SeriesItemViewModel(series: Series) : ViewModelObservable() {

    var id: String = series.id
        @Bindable get

    var abbreviation: String = series.abbreviation
        @Bindable get

    var weblink: String = series.weblink
        @Bindable get

    var created: String = series.created
        @Bindable get
}