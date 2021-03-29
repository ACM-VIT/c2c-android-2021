package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel : ViewModel() {
    private var _loggedIn: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var loggedIn: LiveData<Boolean> = _loggedIn
    private var _error: MutableLiveData<String> = MutableLiveData()
    var error: LiveData<String> = _error
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val verifyEmail:String="Please verify your email address using the link sent."

    fun login(email: String, password: String) {
        if (email != "" && password != "") {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val user: FirebaseUser? = auth.currentUser
                if (user?.isEmailVerified == true) {
                    _loggedIn.value = true
                } else {
                    _error.value = verifyEmail
                }

            }.addOnFailureListener {
                _error.value = it.localizedMessage

            }
        } else {
            _error.value = "Email/Password cannot be empty."
        }
    }

    fun sendEmailVerification() {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener {
            if (it.isSuccessful) {
                _error.value = "Email Verification link sent."
            }
        }
    }

}