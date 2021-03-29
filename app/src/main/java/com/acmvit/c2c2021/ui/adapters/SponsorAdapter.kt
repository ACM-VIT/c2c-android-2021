package com.acmvit.c2c2021.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Sponsor
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.sponsor_item.view.*

class SponsorAdapter(private val sponsorList: List<Sponsor>): RecyclerView.Adapter<SponsorAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.sponsor_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val circularProgressDrawable = CircularProgressDrawable(context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.setColorSchemeColors(getColor(context, R.color.primaryGreen))
        circularProgressDrawable.start()

        Glide.with(context)
            .load(sponsorList[position].imageUrl)
            .placeholder(circularProgressDrawable)
            .into(holder.sponsorImg)

        holder.sponsorImg.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(sponsorList[position].link)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = sponsorList.size

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var sponsorImg: ImageView = itemView.sponsor_img
    }
}