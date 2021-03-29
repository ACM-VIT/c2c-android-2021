package com.acmvit.c2c2021.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.auth.AuthActivity
import com.acmvit.c2c2021.ui.onboarding.JoinDiscordActivity
import com.acmvit.c2c2021.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = FirebaseAuth.getInstance().uid!!
        val viewModel by lazy {
            ViewModelProvider(this, ProfileViewModel.ProfileViewModelFactory(userId)).get(ProfileViewModel::class.java)
        }
        viewModel.user.observe(viewLifecycleOwner, {
            user_name.text = it.name
            email_id.text = it.email
            team_name.text = it.teamName
        })
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }
}