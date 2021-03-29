package com.acmvit.c2c2021.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.Sponsor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SponsorRepository {
    private var list = mutableListOf<Sponsor>()
    private var _sponsorList = MutableLiveData<List<Sponsor>>()
    val sponsorList : LiveData<List<Sponsor>>
        get() = _sponsorList

    fun fetchSponsors() {
        var databaseReference = Firebase.database.getReference("/sponsors")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    list.clear()
                    for(sponsor in snapshot.children) {
                        sponsor.getValue(Sponsor::class.java)?.let { list.add(it) }
                    }
                    _sponsorList.postValue(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("SponsorRep", "SponsorCancelled")
            }

        })
    }
}