package com.acmvit.c2c2021.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.acmvit.c2c2021.R
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val auth: FirebaseAuth = Firebase.auth
    val userCreated:String="User Logged In"
    private var _error: MutableLiveData<String> = MutableLiveData()
    var error: LiveData<String> = _error

    fun checkDatabase(email: String,sharedPreferences: SharedPreferences) {
        if (email != "") {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val reference: DatabaseReference = database.getReference("users")
            reference.orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val editor=sharedPreferences.edit()
                            editor.putString("email",email)
                            editor.apply()
                            sendLink(email)

                        } else {
                            _error.value = getApplication<Application>().getString(R.string.email_not_registered)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        _error.value =  getApplication<Application>().getString(R.string.error)
                    }
                })
        } else {
            _error.value =  getApplication<Application>().getString(R.string.email_empty)
        }


    }

    fun sendLink(email: String) {
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("https://c2c21-bb24e.firebaseapp.com/passwordless?email=email\n")
            .setHandleCodeInApp(true)
            .setAndroidPackageName("com.acmvit.c2c2021",true,null)
            .setIOSBundleId("ak.Code2Create").setDynamicLinkDomain("code2create.page.link")
            .build()

       auth.sendSignInLinkToEmail(email,actionCodeSettings).addOnSuccessListener {
           Log.d("Login","Email sent")
           _error.value= getApplication<Application>().getString(R.string.email_sent)
       }
        .addOnFailureListener {
            Log.d("Login",it.toString())
            _error.value = it.localizedMessage
        }

    }

    fun checkLink(link: String, email: String?){
        if (auth.isSignInWithEmailLink(link)) {
            if(email!=null && email!="") {
                auth.signInWithEmailLink(email, link)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Login", "Successfully signed in with email link!")
                            _error.value = userCreated
                        } else {
                            _error.value =
                                getApplication<Application>().getString(R.string.error_with_link) + task.exception?.localizedMessage
                            Log.e(
                                "Login",
                                "Error signing in with email link",
                                task.exception
                            )
                        }
                    }
            }
            else{
                _error.value=getApplication<Application>().getString(R.string.email_mismatch)
            }
        }
    }

}