package dev.voleum.speedruncom.ui.games

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.adapter.GamesAdapter
import dev.voleum.speedruncom.enum.States

class GamesFragment : Fragment() {

    private lateinit var gamesViewModel: GamesViewModel
    private val adapter = GamesAdapter()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        gamesViewModel = ViewModelProvider(this).get(GamesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_games, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.games_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        checkData()
//        gamesViewModel.load()
//        adapter.addItems(gamesViewModel.data)
//        gamesViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    private fun checkData() {
//        if (view == null) return

        when (gamesViewModel.state) {
            States.CREATED -> {
                gamesViewModel.setListener { checkData() }
                gamesViewModel.load()
            }
            States.PROGRESS -> {
                gamesViewModel.setListener { checkData() }
            }
            States.ERROR -> {
                gamesViewModel.setListener { checkData() }
                gamesViewModel.load()
            }
            States.LOADED -> {
                adapter.addItems(gamesViewModel.data)
            }
        }
    }
}