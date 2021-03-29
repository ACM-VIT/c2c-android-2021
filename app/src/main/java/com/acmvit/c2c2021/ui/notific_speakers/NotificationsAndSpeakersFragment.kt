package com.acmvit.c2c2021.ui.notific_speakers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.databinding.FragmentNotificationsAndSpeakersBinding
import com.acmvit.c2c2021.ui.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class NotificationsAndSpeakersFragment : Fragment() {
    private lateinit var binding:FragmentNotificationsAndSpeakersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_notifications_and_speakers, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var fragments = mutableListOf<Fragment>()
        fragments.add(NotificationFragment())
        fragments.add(SpeakersFragment())
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewpager.adapter = adapter
        val titles = mutableListOf("Notification", "Speakers")
        TabLayoutMediator(binding.notiSpeakersTab, binding.viewpager) { tab, position ->
            tab.text = titles.get(position)
        }.attach()

    }

}