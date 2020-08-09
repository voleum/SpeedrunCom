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

    var items = mutableListOf<Link>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunVideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.holder_run_video, parent, false)
        return RunVideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RunVideoViewHolder, position: Int) {
        //TODO: add all sources (now only youtube)
        val videoId = getYouTubeVideoId(items[position].uri)
        holder.videoWeb.loadData(
            "/*<body style='margin:0;padding:0;'>*/<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${videoId}\" frameborder=\"0\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )
    }

    override fun getItemCount(): Int = items.size

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

    inner class RunVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val videoWeb: WebView = itemView.holder_run_web_view

        init {
            videoWeb.settings.javaScriptEnabled = true
            videoWeb.webChromeClient = WebChromeClient()
        }
    }
}