package com.acmvit.c2c2021.ui.notific_speakers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.databinding.FragmentSpeakersBinding
import com.acmvit.c2c2021.ui.adapters.SpeakersAdapter


class SpeakersFragment : Fragment() {
    private lateinit var binding: FragmentSpeakersBinding
    private val viewModel:SpeakersViewModel by lazy{
        ViewModelProvider(this).get(SpeakersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding=FragmentSpeakersBinding.inflate(inflater,container,false)
        viewModel.list.observe(viewLifecycleOwner, {
            val adapter= SpeakersAdapter(it)
            binding.speakersRecycler.adapter=adapter
        })
        return binding.root
    }

}