package com.acmvit.c2c2021.ui.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.acmvit.c2c2021.R

class WelcomeAdapter(var context: Context) : PagerAdapter() {
    var layoutInflater: LayoutInflater? = null
    var slideImages1 = intArrayOf(
        R.drawable.ic_square,
        R.drawable.ic_calendar_small,
        R.drawable.ic_notification_onboarding,
        R.drawable.ic_category_small,
        R.drawable.ic_information_onboarding
    )
    var slideImages2 = intArrayOf(
        R.drawable.ic_calendar_onboarding,
        R.drawable.ic_notification_large,
        R.drawable.ic_tracks_onboarding,
        R.drawable.ic_document_large,
        R.drawable.ic_profile_onboarding
    )
    var slideImages3 = intArrayOf(
        R.drawable.ic_notification_onboarding,
        R.drawable.ic_category_small,
        R.drawable.ic_information_onboarding,
        R.drawable.ic_profile_small,
        R.drawable.ic_square
    )
    var slideHeader = intArrayOf(
        R.string.welcome_slider_1_header,
        R.string.welcome_slider_2_header,
        R.string.welcome_slider_3_header,
        R.string.welcome_slider_4_header,
        R.string.welcome_slider_5_header
    )
    var slideText = intArrayOf(
        R.string.welcome_slider_1_text,
        R.string.welcome_slider_2_text,
        R.string.welcome_slider_3_text,
        R.string.welcome_slider_4_text,
        R.string.welcome_slider_5_text
    )

    override fun getCount(): Int {
        return 5
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.welcome_slider, container, false)
        val sliderImage1 = view.findViewById<ImageView>(R.id.welcome_slider_image_1)
        val sliderImage2 = view.findViewById<ImageView>(R.id.welcome_slider_image_2)
        val sliderImage3 = view.findViewById<ImageView>(R.id.welcome_slider_image_3)
        val sliderHeader = view.findViewById<TextView>(R.id.welcome_slider_header)
        val sliderText = view.findViewById<TextView>(R.id.welcome_slider_text)
        sliderImage1.setImageResource(slideImages1[position])
        sliderImage2.setImageResource(slideImages2[position])
        sliderImage3.setImageResource(slideImages3[position])
        sliderHeader.setText(slideHeader[position])
        sliderText.setText(slideText[position])
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}