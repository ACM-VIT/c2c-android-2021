package com.acmvit.c2c2021.ui.auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.ActivityAuthBinding
import com.acmvit.c2c2021.ui.MainActivity
import com.acmvit.c2c2021.ui.onboarding.WelcomeActivity
import com.acmvit.c2c2021.viewmodels.AuthViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val overlayDrawable = ColorDrawable(ContextCompat.getColor(this, R.color.loadingOverlay))
        val viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        val sharedPreferences=getSharedPreferences("Pref", MODE_PRIVATE)

        binding.loginButton.setOnClickListener {
            binding.overlayFrame.displayOverlay(true,overlayDrawable)
            binding.progressBarOverlay.visibility= View.VISIBLE
            viewModel.checkDatabase(binding.emailEdittext.text.toString(),sharedPreferences)
        }
        viewModel.error.observe(this, {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            if (it == viewModel.userCreated) {
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            binding.overlayFrame.displayOverlay(false,overlayDrawable)
            binding.progressBarOverlay.visibility= View.INVISIBLE
        })
        var deepLink: Uri?
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    binding.overlayFrame.displayOverlay(true,overlayDrawable)
                    binding.progressBarOverlay.visibility= View.VISIBLE
                    viewModel.checkLink(deepLink.toString(),sharedPreferences.getString("email",""))
                }
            }
            .addOnFailureListener(this) { e -> Log.w("DynamicLink", "getDynamicLink:onFailure", e) }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}
