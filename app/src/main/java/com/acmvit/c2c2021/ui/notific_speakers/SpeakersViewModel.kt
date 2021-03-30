package com.acmvit.c2c2021.ui.notific_speakers

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.acmvit.c2c2021.model.Speakers
import com.acmvit.c2c2021.repository.SpeakersRepository

class SpeakersViewModel: ViewModel() {
    private val repository:SpeakersRepository= SpeakersRepository()
    val list: LiveData<ArrayList<Speakers>> = repository.list
    init{
        repository.fetchData()
    }
}