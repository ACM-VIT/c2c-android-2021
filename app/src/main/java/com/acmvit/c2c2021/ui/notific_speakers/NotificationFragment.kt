package com.acmvit.c2c2021.ui.notific_speakers

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
import com.acmvit.c2c2021.ui.adapters.NotificationAdapter
import com.acmvit.c2c2021.viewmodels.NotificationViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var overlayFrame: OverlayFrame
    private var overlayDrawable: ColorDrawable? = null

    private val viewModel by lazy {
        ViewModelProvider(this).get(NotificationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overlayDrawable =
            ColorDrawable(ContextCompat.getColor(requireContext(), R.color.loadingOverlay))
        progressBar = requireActivity().progress_bar_overlay
        overlayFrame = requireActivity().overlay_frame
        overlayFrame.displayOverlay(true, overlayDrawable!!)
        progressBar.visibility = View.VISIBLE

        viewModel.notificationList.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val adapter = NotificationAdapter(it)
                notification_recyclerView.layoutManager = LinearLayoutManager(requireContext())
                notification_recyclerView.adapter = adapter
                overlayFrame.displayOverlay(false, overlayDrawable!!)
                progressBar.visibility = View.INVISIBLE
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onStop() {
        super.onStop()
        overlayFrame.displayOverlay(false, overlayDrawable!!)
        progressBar.visibility = View.INVISIBLE
    }
}