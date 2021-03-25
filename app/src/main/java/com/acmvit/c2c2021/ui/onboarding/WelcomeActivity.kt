package com.acmvit.c2c2021.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.MainActivity

class WelcomeActivity : AppCompatActivity() {
    var params: LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(
            ViewPager.LayoutParams.WRAP_CONTENT,
            ViewPager.LayoutParams.WRAP_CONTENT
        )
    private var viewPager: ViewPager? = null
    private var welcomeAdapter: WelcomeAdapter? = null
    private var dotsLayout: LinearLayout? = null
    private lateinit var dots: Array<TextView>
    private var next: Button? = null
    private var skip: Button? = null
    private var currentPage = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        params.setMargins(10, 10, 10, 10)
        initViews()
        welcomeAdapter = WelcomeAdapter(this)
        viewPager!!.adapter = welcomeAdapter
        viewPager!!.addOnPageChangeListener(viewListener)
        addDotsIndicator(0)
        skipListener()
        nextListener()
    }

    private fun addDotsIndicator(position: Int) {
        dots = Array(5) { TextView(this) }
        dotsLayout!!.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226")
            dots[i].textSize = 2f
            dots[i].setBackgroundResource(R.drawable.ic_circle_bordered)
            dots[i].setLayoutParams(params)
            dotsLayout!!.addView(dots[i])
        }
        if (dots.size > 0) {
            dots[2 * position].setBackgroundResource(R.drawable.ic_circle_filled)
        }
    }

    private fun launchActivity() {
        startActivity(Intent(this@WelcomeActivity, JoinDiscordActivity::class.java))
        finish()
    }


    private fun nextListener() {
        next!!.setOnClickListener { v: View? ->
            if (currentPage < 2) viewPager!!.currentItem = currentPage + 1 else {
                next!!.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.textColor
                    )
                )
                next!!.setEnabled(false);
                launchActivity()
            }
        }
    }

    private fun skipListener() {
        skip!!.setOnClickListener { v: View? ->
            /*skip.setEnabled(false);*/skip!!.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorGrey
            )
        )
            launchActivity()
        }
    }

    var viewListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            addDotsIndicator(position)
            currentPage = position
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun initViews() {
        viewPager = findViewById(R.id.vp_welcome)
        dotsLayout = findViewById(R.id.dots_layout)
        next = findViewById(R.id.next)
        skip = findViewById(R.id.skip)
    }

}