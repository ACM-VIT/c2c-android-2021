package com.acmvit.c2c2021.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentLoginBinding
import com.acmvit.c2c2021.ui.onboarding.WelcomeActivity
import com.acmvit.c2c2021.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginButton.setOnClickListener {
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
        })
        viewModel.loggedIn.observe(viewLifecycleOwner, {
            if (it) {
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