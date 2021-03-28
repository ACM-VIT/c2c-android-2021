package com.acmvit.c2c2021.ui.info_sponsers

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class InfoSponsorAdapter(fragmentManager: FragmentManager,
                         lifecycle: Lifecycle,
                         private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = fragments[position]
}