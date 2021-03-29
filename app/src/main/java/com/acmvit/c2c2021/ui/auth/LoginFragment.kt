package com.acmvit.c2c2021.ui.auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentLoginBinding
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.ui.onboarding.WelcomeActivity
import com.acmvit.c2c2021.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private lateinit var binding: FragmentLoginBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private  var overlayDrawable: ColorDrawable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        overlayDrawable = activity?.let {
            ContextCompat.getColor(
                it,
                R.color.loadingOverlay
            )
        }?.let { ColorDrawable(it) }
        progressBar= activity?.findViewById(R.id.progress_bar_overlay)!!
        overlayFrame= activity?.findViewById(R.id.overlay_frame)!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginButton.setOnClickListener {
            overlayFrame.displayOverlay(true, overlayDrawable!!)
            progressBar.visibility = View.VISIBLE
            viewModel.login(
                binding.emailEdittext.text.toString(),
                binding.passwordEdittext.text.toString()
            )
        }
        binding.signup.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        viewModel.error.observe(viewLifecycleOwner, {
            if (it == viewModel.verifyEmail) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).setAction("Resend Mail"
                ) { viewModel.sendEmailVerification() }.show()
            }
            else {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
            overlayFrame.displayOverlay(false,overlayDrawable!!)
            progressBar.visibility = View.INVISIBLE
        })
        viewModel.loggedIn.observe(viewLifecycleOwner, {
            if (it) {
                overlayFrame.displayOverlay(false,overlayDrawable!!)
                progressBar.visibility = View.INVISIBLE
                updateUI()
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = viewModel.auth.currentUser
        if (currentUser != null) {
            if (currentUser.isEmailVerified)
                updateUI()
        }
    }

    private fun updateUI() {
        startActivity(Intent(activity, WelcomeActivity::class.java))
    }

}