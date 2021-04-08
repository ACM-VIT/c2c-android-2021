package com.acmvit.c2c2021.ui.adapters

import android.annotation.SuppressLint
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.TimelineItem
import kotlinx.android.synthetic.main.timeline_item.view.*
import java.text.SimpleDateFormat

class TimelineAdapter(private val timelineList: List<TimelineItem>) :
    RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineAdapter.ViewHolder {
        return TimelineAdapter.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.timeline_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TimelineAdapter.ViewHolder, position: Int) {
        holder.timelineTitle.text = timelineList[position].title
        holder.timelineSubTitle.text = timelineList[position].subtitle
        val dateSplits = dateParser(timelineList[position].startUnix)
        val dateHolder = dateSplits.split(" ")
        holder.timelineDay.text = dateHolder[0]
        holder.timelineDate.text = "${dateHolder[1]}${dateSuffixes[dateHolder[1].toInt()]}"
        holder.timelineTimingText.text =
            "${unixToHour(timelineList[position].startUnix)}-${unixToHour(timelineList[position].endUnix)}"
        holder.currentEventInidcator.setImageResource(
            if (isEventCurrentlyInProgress(
                    timelineList[position].startUnix,
                    timelineList[position].endUnix
                )
            ) R.drawable.ic_circle_filled else R.drawable.ic_circle_bordered
        )
    }

    override fun getItemCount(): Int = timelineList.size

    @SuppressLint("SimpleDateFormat")
    private fun dateParser(unix: Long): String {
        val formatter = SimpleDateFormat("EEE dd")
        return formatter.format(unix * 1000)
    }

    @SuppressLint("SimpleDateFormat")
    private fun unixToHour(unix: Long): String {
        val formatter = SimpleDateFormat("haaa")
        return formatter.format(unix * 1000)
    }

    private fun isEventCurrentlyInProgress(startUnix: Long, endUnix: Long): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        if (currentTime in (startUnix + 1) until endUnix) {
            return true
        }
        return false
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var timelineTitle: TextView = itemView.timeline_item_title
        var timelineSubTitle: TextView = itemView.timeline_item_subtext
        var timelineTimingText: TextView = itemView.timeline_item_timing_text
        var timelineDay: TextView = itemView.timeline_item_day
        var timelineDate: TextView = itemView.timeline_item_date
        var currentEventInidcator = itemView.timeline_current_indicator
    }

    companion object {
        var dateSuffixes = arrayOf(
            "th",
            "st",
            "nd",
            "rd",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "st",
            "nd",
            "rd",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "th",
            "st"
        )
    }

}