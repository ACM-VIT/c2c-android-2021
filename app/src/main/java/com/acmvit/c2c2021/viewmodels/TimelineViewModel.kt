package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import com.acmvit.c2c2021.repository.TimelineRepository

class TimelineViewModel : ViewModel() {
    private var timelineRepository = TimelineRepository()
    val timelineList = timelineRepository.timelineList

    init {
        timelineRepository.fetchTimeline()
    }
}