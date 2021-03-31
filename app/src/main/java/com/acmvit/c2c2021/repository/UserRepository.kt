package com.acmvit.c2c2021.repository

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_join_discord.*

class UserRepository {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private var _discordLink = MutableLiveData<String>()
    val discordLink: LiveData<String>
        get() = _discordLink

    fun fetchUser(email: String) {
        val databaseReference = Firebase.database.getReference("users")
        Log.d("Email",email)
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    val child=snapshot.children.elementAt(0)
                    child.getValue(User::class.java) .let {
                            _user.postValue(it)
                        }

                    }
                }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UserRep", "UserCancelled")
            }

        })
    }

    fun fetchDiscord() {
        val databaseReference = Firebase.database.getReference("/utils/discord")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()) {
                    snapshot.getValue(String::class.java).let {
                        _discordLink.postValue(it)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.i("UserRepo", "DiscordCancelled")
            }
        })
    }
}