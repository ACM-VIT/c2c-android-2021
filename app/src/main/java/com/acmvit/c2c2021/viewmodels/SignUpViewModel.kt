package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SignUpViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val userCreated:String="User Created.Email Verification Link sent."
    private var _error: MutableLiveData<String> = MutableLiveData()
    var error: LiveData<String> = _error

    fun signUp(email: String, password: String) {
        if (email != "" && password != "") {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.getReference("users")
            reference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            createNewUser(email, password)
                        } else {
                            _error.value = "Please use the emailID registered on HackerEarth."
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        _error.value = "Some error occurred."
                    }
                })
        } else {
            _error.value = "Email/Password cannot be empty."
        }


    }

    fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    _error.value = userCreated
                } else {
                    _error.value = "Error sending email verification link."
                }
            }
        }.addOnFailureListener {
            _error.value = it.localizedMessage
        }

    }

}