package dev.voleum.speedruncom.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameViewModel =
            ViewModelProvider(this).get(GameViewModel::class.java)
        arguments?.apply {
            gameViewModel.text = getString("game", "")
        }
        val binding: FragmentGameBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        binding.viewModel = gameViewModel
        return binding.root
    }
}