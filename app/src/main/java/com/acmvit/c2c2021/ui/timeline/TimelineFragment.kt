package com.acmvit.c2c2021.ui.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.adapters.TimelineAdapter
import com.acmvit.c2c2021.viewmodels.TimelineViewModel
import kotlinx.android.synthetic.main.fragment_timeline.*


class TimelineFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(TimelineViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.timelineList.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val adapter = TimelineAdapter(it)
                timleline_recyclerView.layoutManager = LinearLayoutManager(requireContext())
                timleline_recyclerView.adapter = adapter
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

}