package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable

class GamesItemViewModel(game: Game) : ViewModelObservable() {

    var id: String = game.id
        @Bindable get

    var abbreviation: String = game.abbreviation
        @Bindable get

    var weblink: String = game.weblink
        @Bindable get

    var released: String = game.released.toString()
        @Bindable get

    var romhack: String = game.romhack.toString()
        @Bindable get

    var created: String = game.created
        @Bindable get
}