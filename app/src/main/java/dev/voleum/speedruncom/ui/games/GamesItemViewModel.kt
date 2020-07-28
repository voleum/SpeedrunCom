package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable

class GamesItemViewModel(game: Game) : ViewModelObservable() {

    var imageUrl: String = game.assets.coverLarge.uri

    var name: String = game.names.international
        @Bindable get

    var released: String = game.released.toString()
        @Bindable get
}