package com.acmvit.c2c2021.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Prize
import java.util.*

class PrizesAdapter(private val prizes: ArrayList<Prize>) :
    RecyclerView.Adapter<PrizesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.prizes_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageResource.setImageResource(prizes[position].prizeImage)
        holder.name.text = prizes[position].prizeName
        holder.money.text = prizes[position].prizeMoney
    }

    override fun getItemCount(): Int {
        return prizes.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageResource: ImageView = itemView.findViewById(R.id.trophy)
        var name: TextView = itemView.findViewById(R.id.prize_name)
        var money: TextView = itemView.findViewById(R.id.prize_money)
    }

}