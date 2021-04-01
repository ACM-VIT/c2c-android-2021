package com.acmvit.c2c2021.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.NotificationItem
import kotlinx.android.synthetic.main.noitifcation_item.view.*
import kotlinx.android.synthetic.main.timeline_item.view.*
import java.text.SimpleDateFormat

class NotificationAdapter(private val notificationList: List<NotificationItem>):
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        context = parent.context
        return NotificationAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.noitifcation_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.notificationTitle.text = notificationList[position].message
        holder.notificationSinceTime.text = toSinceTime(notificationList[position].time)
    }

    override fun getItemCount(): Int = notificationList.size

    private fun toSinceTime(unix: Long): String{
        return  DateUtils.getRelativeDateTimeString(context, unix ,
            DateUtils.MINUTE_IN_MILLIS,DateUtils.WEEK_IN_MILLIS,0).split(",")[0]
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var notificationTitle: TextView = itemView.notification_item_title
        var notificationSinceTime: TextView = itemView.noitifaction_since_time

    }
}