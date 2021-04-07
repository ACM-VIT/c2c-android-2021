package com.acmvit.c2c2021.ui.timeline

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.OverlayFrame
import com.acmvit.c2c2021.ui.adapters.TimelineAdapter
import com.acmvit.c2c2021.viewmodels.TimelineViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_timeline.*
import android.util.Log.d


class TimelineFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private var overlayDrawable: ColorDrawable? = null

    private var goToPosReciever: Int = 0

    private val viewModel by lazy {
        ViewModelProvider(this).get(TimelineViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overlayDrawable =
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.loadingOverlay))
        progressBar = requireActivity().progress_bar_overlay
        overlayFrame = requireActivity().overlay_frame
        overlayFrame.displayOverlay(true, overlayDrawable!!)
        progressBar.visibility = View.VISIBLE
        viewModel.timelineList.observe(viewLifecycleOwner, { it ->
            if (it.isNotEmpty()) {
                val adapter = TimelineAdapter(it)
                it.forEach { item ->
                    if(isEventCurrentlyInProgress(item.startUnix, item.endUnix)){
                        goToPosReciever = it.indexOf(item)
                    }
                }
                d("TimelineItem", "$goToPosReciever")
                val linearLayoutManager = LinearLayoutManager(requireContext())
                linearLayoutManager.scrollToPositionWithOffset(goToPosReciever, 20)
                timleline_recyclerView.layoutManager = linearLayoutManager
                timleline_recyclerView.adapter = adapter
                overlayFrame.displayOverlay(false, overlayDrawable!!)
                progressBar.visibility = View.INVISIBLE
//                timleline_recyclerView.smoothScrollToPosition(goToPosReciever)
            }
        })
    }

    private fun isEventCurrentlyInProgress(startUnix: Long, endUnix: Long): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        if (currentTime in (startUnix + 1) until endUnix) {
            return true
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onStop() {
        super.onStop()
        overlayFrame.displayOverlay(false, overlayDrawable!!)
        progressBar.visibility = View.INVISIBLE
    }
}