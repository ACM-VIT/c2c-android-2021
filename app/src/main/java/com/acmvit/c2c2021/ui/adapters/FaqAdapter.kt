package com.acmvit.c2c2021.ui.adapters

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.model.Faq
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.faq_item.view.*

class FaqAdapter: RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var faqList = mutableListOf<Faq>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.question.text = faqList[position].question
        holder.answer.text = faqList[position].answer
        holder.button.setImageResource(R.drawable.ic_plus)
        holder.layout.setBackgroundResource(R.drawable.ic_background_rectangle)
        holder.answer.visibility = View.GONE
        holder.cardView.setOnClickListener {
            if(holder.answer.visibility == View.VISIBLE) {
                TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                holder.answer.visibility = View.GONE
                holder.button.setImageResource(R.drawable.ic_plus)
                holder.layout.setBackgroundResource(R.drawable.ic_background_rectangle)
            }else {
                TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                holder.answer.visibility = View.VISIBLE
                holder.button.setImageResource(R.drawable.ic_minus)
                holder.layout.setBackgroundResource(R.drawable.primary_button_ripple_effect)
            }
        }
    }

    override fun getItemCount(): Int = faqList.size

    fun submitList(list: List<Faq>) {
        faqList.clear()
        for(faq in list) {
            faq.let {
                faqList.add(it)
            }
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var question: TextView = itemView.faq_qs
        var answer: TextView = itemView.faq_ans
        var button: ImageView = itemView.expand_btn
        var cardView: MaterialCardView = itemView.faq_card
        var layout: ConstraintLayout = itemView.faq_item_layout
    }
}