package dev.voleum.speedruncom.ui.screen

import androidx.databinding.Bindable
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable

class GameViewModel : ViewModelObservable() {

    var game: Game? = null
        @Bindable get
        @Bindable set

    var text: String = ""
        @Bindable get
        @Bindable set
}