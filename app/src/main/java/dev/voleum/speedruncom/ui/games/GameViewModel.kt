package dev.voleum.speedruncom.ui.games

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import dev.voleum.speedruncom.model.Game
import dev.voleum.speedruncom.ui.ViewModelObservable

class GameViewModel : ViewModelObservable() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is game Fragment"
    }

    var game: Game? = null
        @Bindable get
        @Bindable set

    var text: String = ""
        @Bindable get
        @Bindable set
}