package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.repository.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileViewModel: ViewModel() {

    private val userRepository = UserRepository()
    val user = userRepository.user
    val discordLink = userRepository.discordLink

    init {
        userRepository.fetchDiscord()
        userRepository.fetchUser(Firebase.auth.currentUser?.email!!)
    }
}