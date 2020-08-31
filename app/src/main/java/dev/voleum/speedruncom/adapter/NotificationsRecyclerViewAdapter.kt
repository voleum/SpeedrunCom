package dev.voleum.speedruncom.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.databinding.HolderNotificationBinding
import dev.voleum.speedruncom.model.Notification

class NotificationsRecyclerViewAdapter :
    RecyclerView.Adapter<NotificationsRecyclerViewAdapter.NotificationViewHolder>() {

    init {
        setHasStableIds(true)
    }

    val items = mutableListOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.holder_notification,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) =
        holder.bind(position)

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long =
        items[position].hashCode().toLong()

    fun replaceItems(items: List<Notification>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(val binding: HolderNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
//            binding.viewModel = NotificationsItemViewModel(items[position])
            val textView = binding.root.findViewById<TextView>(R.id.holder_notification_text_view)
            textView.text =
                Html.fromHtml(
                    items[position]
                        .text
                        .replace("<span class=\"bolder\">", "<b>")
                        .replace("</span>", "</b>"))
        }
    }
}