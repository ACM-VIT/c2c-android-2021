package com.acmvit.c2c2021.ui.profile

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.ui.auth.AuthActivity
import com.acmvit.c2c2021.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    private lateinit var discordLink: String
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private  var overlayDrawable: ColorDrawable? = null

    val viewModel by lazy {
        ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        overlayDrawable = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.loadingOverlay))
        progressBar = requireActivity().progress_bar_overlay
        overlayFrame = requireActivity().overlay_frame
        overlayFrame.displayOverlay(true, overlayDrawable!!)
        progressBar.visibility = View.VISIBLE

        viewModel.user.observe(viewLifecycleOwner, {
            it.let {
                user_name.text = it.name
                email_id.text = it.email
                team_name.text = it.teamName
                overlayFrame.displayOverlay(false, overlayDrawable!!)
                progressBar.visibility = View.INVISIBLE
            }
        })

        join_discord_card.setOnClickListener {
            val intent = Intent(ACTION_VIEW)
            intent.data = Uri.parse(discordLink)
            requireContext().startActivity(intent)
        }

        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        viewModel.discordLink.observe(viewLifecycleOwner) {
            discordLink = it
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onStop(){
        super.onStop()
        overlayFrame.displayOverlay(false, overlayDrawable!!)
        progressBar.visibility = View.INVISIBLE
    }

}