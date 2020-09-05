package dev.voleum.speedruncom.ui

import androidx.fragment.app.Fragment

abstract class AbstractFragment<VM: ViewModelObservable, B: Any> : Fragment() {

    lateinit var viewModel: VM
    lateinit var binding: B

}
