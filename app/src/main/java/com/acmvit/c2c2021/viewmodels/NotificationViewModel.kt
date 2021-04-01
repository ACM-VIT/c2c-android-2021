package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import com.acmvit.c2c2021.repository.NotificationRepository

class NotificationViewModel : ViewModel() {
    private var notificationRepository = NotificationRepository()
    val notificationList = notificationRepository.notificationsList

    init {
        notificationRepository.fetchNotifications()
    }
}