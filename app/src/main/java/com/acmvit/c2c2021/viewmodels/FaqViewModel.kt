package com.acmvit.c2c2021.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acmvit.c2c2021.repository.FaqRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FaqViewModel: ViewModel() {
    private var faqRepository = FaqRepository()
    val faqList = faqRepository.faqList

    init {
        faqRepository.fetchFaqs()
    }
}