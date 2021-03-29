package com.acmvit.c2c2021.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.Faq
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FaqRepository {

    private var list = mutableListOf<Faq>()
    private var _faqList = MutableLiveData<List<Faq>>()
    val faqList : LiveData<List<Faq>>
        get() = _faqList

    fun fetchFaqs() {
        var databaseReference = Firebase.database.getReference("/FAQs")
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    list.clear()
                    for(faq in snapshot.children) {
                        faq.getValue(Faq::class.java)?.let { list.add(it) }
                    }
                    _faqList.postValue(list)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FaqRep", "FaqCancelled")
            }

        })
    }
}