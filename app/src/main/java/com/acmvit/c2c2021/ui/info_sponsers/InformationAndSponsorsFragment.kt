package com.acmvit.c2c2021.ui.info_sponsers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acmvit.c2c2021.databinding.FragmentInformationAndSponsorsBinding
import com.acmvit.c2c2021.ui.adapters.ViewPagerAdapter
import com.acmvit.c2c2021.ui.info_sponsersView.SponsersFragment
import com.google.android.material.tabs.TabLayoutMediator

class InformationAndSpeakersFragment : Fragment() {

    private var _binding: FragmentInformationAndSponsorsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInformationAndSponsorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var fragments = mutableListOf<Fragment>()
        fragments.add(InformationFragment())
        fragments.add(SponsersFragment())
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.infoSponsorVp.adapter = adapter
        val titles = mutableListOf("Information", "Sponsors")
        TabLayoutMediator(binding.infoSponsorTab, binding.infoSponsorVp) { tab, position ->
            tab.text = titles.get(position)
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}