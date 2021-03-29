package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.acmvit.c2c2021.repository.UserRepository

class ProfileViewModel(private var uid: String): ViewModel() {

    private val userRepository = UserRepository()
    val user = userRepository.user
    val discordLink = userRepository.discordLink
    private fun fetchUser() {
        userRepository.fetchUser(uid)
    }

    class ProfileViewModelFactory(private val userId: String) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}