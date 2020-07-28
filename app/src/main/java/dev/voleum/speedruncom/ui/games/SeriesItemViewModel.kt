package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Series
import dev.voleum.speedruncom.ui.ViewModelObservable

class SeriesItemViewModel(series: Series) : ViewModelObservable() {

    var imageUrl: String = series.assets.coverLarge.uri

    var name: String = series.names.international
        @Bindable get
}