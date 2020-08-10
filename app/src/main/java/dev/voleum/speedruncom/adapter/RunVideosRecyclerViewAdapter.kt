package dev.voleum.speedruncom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.model.Link
import kotlinx.android.synthetic.main.holder_run_video.view.*

class RunVideosRecyclerViewAdapter : RecyclerView.Adapter<RunVideosRecyclerViewAdapter.RunVideoViewHolder>() {

    init {
        setHasStableIds(true)
    }

    var items = mutableListOf<Link>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunVideoViewHolder {
//        RunVideoViewHolder(
//            DataBindingUtil.inflate(
//                LayoutInflater.from(parent.context),
//                R.layout.holder_run_video,
//                parent,
//                false
//            )
//        )
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.holder_run_video,
                parent,
                false)
        return RunVideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunVideoViewHolder, position: Int) {
        //TODO: add all sources (now only youtube)
//        holder.binding.viewModel = RunVideoItemViewModel()
        val videoId = getYouTubeVideoId(items[position].uri)
        holder.loadData(videoId)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun replaceItems(items: List<Link>) {
        this.items.clear()
        this.items.addAll(items)
//        notifyDataSetChanged()
    }

    fun getYouTubeVideoId(link: String): String {
        val videoIdIndex =
            if (link.indexOf("youtube.com") != -1) link.indexOf("v=") + 2
            else link.indexOf("youtu.be/") + 9
        return link.substring(videoIdIndex, videoIdIndex + 11)
    }

//    inner class RunVideoViewHolder(val binding: HolderRunVideoBinding) : RecyclerView.ViewHolder(binding.root) {
    inner class RunVideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val videoWeb: WebView = view.holder_run_web_view

        init {
            videoWeb.settings.javaScriptEnabled = true
            videoWeb.webChromeClient = WebChromeClient()
        }

        fun loadData(videoId: String) {
            videoWeb.loadData(
                "<body style='margin:0;padding:0;'><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${videoId}\" frameborder=\"0\" allowfullscreen></iframe>",
                "text/html",
                "utf-8"
            )
        }
    }
}