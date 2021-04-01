package com.acmvit.c2c2021.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.NotificationItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class NotificationRepository {
    private var list = mutableListOf<NotificationItem>()
    private var _notificationsList = MutableLiveData<List<NotificationItem>>()
    val notificationsList: LiveData<List<NotificationItem>>
        get() = _notificationsList

    fun fetchNotifications() {
        val databaseReference = Firebase.database.getReference("/notifications")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    list.clear()
                    for (timelineData in snapshot.children) {
                        timelineData.getValue(NotificationItem::class.java)?.let { list.add(it) }
                    }
                    _notificationsList.postValue(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("NotificationRepo", "Notifications Fetch Cancelled")
            }

        })
    }
}