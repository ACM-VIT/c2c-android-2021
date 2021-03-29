package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import com.acmvit.c2c2021.repository.SponsorRepository

class SponsorViewModel: ViewModel() {
    private var sponsorRepository = SponsorRepository()
    val sponsorList = sponsorRepository.sponsorList

    init {
        sponsorRepository.fetchSponsors()
    }
}