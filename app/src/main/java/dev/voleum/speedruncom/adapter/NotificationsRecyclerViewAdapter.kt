package dev.voleum.speedruncom.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.voleum.speedruncom.R
import dev.voleum.speedruncom.model.Notification
import kotlinx.android.synthetic.main.holder_notification.view.*

class NotificationsRecyclerViewAdapter :
    RecyclerView.Adapter<NotificationsRecyclerViewAdapter.NotificationViewHolder>() {

    init {
        setHasStableIds(true)
    }

    val items = mutableListOf<Notification>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.holder_notification, parent, false)
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

    inner class NotificationViewHolder(var view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
//            binding.viewModel = NotificationsItemViewModel(items[position])
            val textView = view.holder_notification_text_view
            textView.alpha =
                if (items[position].status == "read") 0.5F
                else 1.0F
            textView.text =
                Html.fromHtml(
                    items[position]
                        .text
                        .replace("<span class=\"bolder\">", "<b>")
                        .replace("</span>", "</b>")
                )
        }
    }
}