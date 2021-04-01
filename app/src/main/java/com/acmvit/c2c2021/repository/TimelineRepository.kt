package com.acmvit.c2c2021.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.TimelineItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class TimelineRepository {
    private var list = mutableListOf<TimelineItem>()
    private var _timelineList = MutableLiveData<List<TimelineItem>>()
    val timelineList: LiveData<List<TimelineItem>>
        get() = _timelineList

    fun fetchTimeline() {
        val databaseReference = Firebase.database.getReference("/timeline")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    list.clear()
                    for (timelineData in snapshot.children) {
                        timelineData.getValue(TimelineItem::class.java)?.let { list.add(it) }
                    }
                    _timelineList.postValue(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("TimelineRepo", "Timeline Fetch Cancelled")
            }

        })
    }
}