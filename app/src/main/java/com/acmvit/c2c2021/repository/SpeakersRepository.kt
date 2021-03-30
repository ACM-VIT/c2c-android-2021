package com.acmvit.c2c2021.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.Speakers
import com.google.firebase.database.*

class SpeakersRepository {
    private val _list: MutableLiveData<ArrayList<Speakers>> = MutableLiveData()
    val list: LiveData<ArrayList<Speakers>> = _list
    var arrayList:ArrayList<Speakers> = ArrayList()

    fun fetchData(){
        _list.value=arrayList
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.getReference("speakers")
        reference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(speaker in snapshot.children){
                    speaker.getValue(Speakers::class.java)?.let{
                        arrayList.add(it)
                    }
                }
                _list.postValue(arrayList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase",error.message)
            }

        })

    }
}