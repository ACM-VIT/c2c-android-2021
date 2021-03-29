package com.acmvit.c2c2021.ui.auth

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
import com.acmvit.c2c2021.databinding.FragmentSignupBinding
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.viewmodels.SignUpViewModel
import com.google.android.material.snackbar.Snackbar


class SignUpFragment : Fragment() {

    private lateinit var binding:FragmentSignupBinding
    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this).get(SignUpViewModel::class.java)
    }
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private  var overlayDrawable: ColorDrawable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_signup,container,false)
        viewModel.error.observe(viewLifecycleOwner, {
            overlayFrame.displayOverlay(false,overlayDrawable!!)
            progressBar.visibility = View.INVISIBLE
            Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            if(it==viewModel.userCreated){
                Navigation.findNavController(binding.root).navigate(R.id.action_signUpFragment_to_loginFragment)
            }
        })
        binding.signupButton.setOnClickListener {
            overlayFrame.displayOverlay(true, overlayDrawable!!)
            progressBar.visibility = View.VISIBLE
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

}