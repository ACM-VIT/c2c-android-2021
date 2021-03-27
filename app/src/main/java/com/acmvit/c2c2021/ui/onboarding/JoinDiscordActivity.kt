package com.acmvit.c2c2021.ui.onboarding

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.MainActivity
import com.acmvit.c2c2021.ui.OverlayFrame
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_join_discord.*

class JoinDiscordActivity : AppCompatActivity() {
    var discordLink: String? = null
    var overlayFrame: OverlayFrame? = null
    var progressBar: ProgressBar? = null
    private var overlayDrawable: ColorDrawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_discord)

        overlayDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.loadingOverlay))
        overlayFrame = findViewById(R.id.overlay_frame)
        progressBar = findViewById(R.id.progress_bar_overlay)
        overlayFrame?.displayOverlay(true, overlayDrawable!!)
        progressBar?.visibility = View.VISIBLE
        fetchData()
        disc_skip_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        join_discord_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(discordLink))
            val chooser = Intent.createChooser(intent, "Open Discord Link With")
            startActivity(chooser)
        }
    }

    private fun fetchData() {
        val ref1 = FirebaseDatabase.getInstance().getReference("/utils/discord")
        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                discordLink = dataSnapshot.getValue(String::class.java)
                if (discordLink == null) {
                    join_discord_btn.isEnabled = false
                }
                overlayFrame?.displayOverlay(false, overlayDrawable!!)
                progressBar?.visibility = View.INVISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("JoinDiscordActivity", "Can't fetch details")
            }
        })
    }

}