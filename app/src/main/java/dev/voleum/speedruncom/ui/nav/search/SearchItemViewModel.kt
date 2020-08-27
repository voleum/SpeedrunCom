package dev.voleum.speedruncom.ui.nav.search

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Asset
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable

class SearchItemViewModel(val item: Game) : ViewModelObservable() {

    val name: String
        @Bindable get() = item.names.international

    val imageAsset: Asset
        get() = item.assets.coverSmall
}