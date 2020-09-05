package dev.voleum.speedruncom.ui

import androidx.databinding.Bindable

abstract class AbstractViewModel : ViewModelObservable() {

    var isLoaded = false
        @Bindable get
}