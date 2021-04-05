package com.acmvit.c2c2021.ui.adapters

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Speakers
import com.bumptech.glide.Glide


class SpeakersAdapter(private val list:ArrayList<Speakers>): RecyclerView.Adapter<SpeakersAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.speakers_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text= list[position].name
        holder.title.text= list[position].topic
        Glide.with(holder.image)
            .load(list[position].imageUrl)
            .placeholder(R.color.primaryGreen)
            .into(holder.image)
        val currentTime:Long=System.currentTimeMillis()/1000
        if(list[position].startUnix>currentTime){
            holder.status.setBackgroundColor(Color.parseColor("#F2C94C"))
            holder.status.text=holder.status.context.resources.getString(R.string.coming_soon)
        }
        if(list[position].startUnix<=currentTime && list[position].endUnix>=currentTime){
            holder.status.setBackgroundColor(Color.parseColor("#48BA86"))
            holder.status.text=holder.status.context.resources.getString(R.string.live_now)
            holder.itemView.setOnClickListener {
                if(list[position].sessionUrl!="") {
                    val uri = Uri.parse(list[position].sessionUrl)
                    holder.itemView.context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }
        }
        if(list[position].endUnix<currentTime){
            holder.status.setBackgroundColor(Color.parseColor("#EB5757"))
            holder.status.text=holder.status.context.resources.getString(R.string.event_over)
        }
    }

    override fun getItemCount(): Int = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image:ImageView=itemView.findViewById(R.id.speaker_img)
        val name:TextView=itemView.findViewById(R.id.speaker_name)
        val title:TextView=itemView.findViewById(R.id.speaker_topic)
        val status:TextView=itemView.findViewById(R.id.event_status)
    }
}