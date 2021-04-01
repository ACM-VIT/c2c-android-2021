package com.acmvit.c2c2021.ui.notific_speakers

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentSpeakersBinding
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.ui.adapters.SpeakersAdapter
import kotlinx.android.synthetic.main.activity_main.*


class SpeakersFragment : Fragment() {
    private lateinit var binding: FragmentSpeakersBinding
    private val viewModel:SpeakersViewModel by lazy{
        ViewModelProvider(this).get(SpeakersViewModel::class.java)
    }
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private lateinit var overlayDrawable: ColorDrawable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding=FragmentSpeakersBinding.inflate(inflater,container,false)
        overlayDrawable = ColorDrawable(ContextCompat.getColor(requireContext(), R.color.loadingOverlay))
        progressBar = requireActivity().progress_bar_overlay
        overlayFrame = requireActivity().overlay_frame
        overlayFrame.displayOverlay(true, overlayDrawable)
        progressBar.visibility = View.VISIBLE

        viewModel.list.observe(viewLifecycleOwner, {
            val adapter= SpeakersAdapter(it)
            if(it.size!=0){
                overlayFrame.displayOverlay(false, overlayDrawable)
                progressBar.visibility = View.INVISIBLE
            }
            binding.speakersRecycler.adapter=adapter
        })
        return binding.root
    }
    override fun onStop(){
        super.onStop()
        overlayFrame.displayOverlay(false, overlayDrawable)
        progressBar.visibility = View.INVISIBLE
    }

}