package com.acmvit.c2c2021.ui.info_sponsers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R

class AboutAdapter(private val list: List<String>): RecyclerView.Adapter<AboutAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.about_slider, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = 2

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}