package com.acmvit.c2c2021.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentSignupBinding
import com.acmvit.c2c2021.ui.MainActivity
import com.acmvit.c2c2021.viewmodels.SignUpViewModel
import com.google.android.material.snackbar.Snackbar


class SignUpFragment : Fragment() {

    private lateinit var binding:FragmentSignupBinding
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_signup,container,false)
        viewModel.error.observe(viewLifecycleOwner, {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            if(it==viewModel.userCreated){
                Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        })
        binding.signupButton.setOnClickListener {
            viewModel.signUp(
                binding.emailEdittext.text.toString(),
                binding.passwordEdittext.text.toString()
            )
        }
        binding.login.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        return binding.root
    }

}