package com.acmvit.c2c2021.ui.info_sponsersView

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.databinding.FragmentSponsersBinding
import com.acmvit.c2c2021.ui.adapters.SponsorAdapter
import com.acmvit.c2c2021.viewmodels.SponsorViewModel
import kotlinx.android.synthetic.main.fragment_sponsers.*

class SponsersFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(SponsorViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding:FragmentSponsersBinding= FragmentSponsersBinding.inflate(inflater)
        binding.titleSponsor.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.zuddl.com/")))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sponsorList.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
                val adapter = SponsorAdapter(it)
                rv_sponsors.adapter = adapter
            }
        })
    }
}