package com.acmvit.c2c2021.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.About
import kotlinx.android.synthetic.main.about_slider.view.*

class AboutAdapter(private val list: List<About>): RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    private lateinit var context: Context;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.about_slider, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.aboutHeader.text = list[position].heading
        holder.aboutContent.text = list[position].content
        holder.website.setOnClickListener{
            launchUrl(list[position].webUrl)
        }
        holder.instagram.setOnClickListener{
            launchUrl(list[position].instaUrl)
        }
        holder.linkedIn.setOnClickListener{
            launchUrl(list[position].linkedInUrl)
        }
    }

    private fun launchUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int = 4

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var aboutHeader: TextView = itemView.tv_about
        var aboutContent: TextView = itemView.about_content
        var website: ImageView = itemView.website
        var instagram: ImageView = itemView.instagram
        var linkedIn: ImageView = itemView.linkedin
    }
}