package com.acmvit.c2c2021.ui.notific_speakers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.acmvit.c2c2021.R
import com.acmvit.c2c2021.ui.adapters.NotificationAdapter
import com.acmvit.c2c2021.viewmodels.NotificationViewModel
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(NotificationViewModel::class.java)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.notificationList.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                val adapter = NotificationAdapter(it)
                notification_recyclerView.layoutManager = LinearLayoutManager(requireContext())
                notification_recyclerView.adapter = adapter
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

}