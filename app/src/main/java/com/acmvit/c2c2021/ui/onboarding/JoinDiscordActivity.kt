package com.acmvit.c2c2021.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.MainActivity
import kotlinx.android.synthetic.main.activity_join_discord.*

class JoinDiscordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_discord)

        disc_skip_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}