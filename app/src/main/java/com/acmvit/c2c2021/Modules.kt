package com.acmvit.c2c2021

import com.acmvit.c2c2021.data.FirebaseRTD
import com.acmvit.c2c2021.data.ResourcesRepository
import com.acmvit.c2c2021.ui.tracks.TracksViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { Firebase.database.reference }
    single { FirebaseRTD(get()) }
}

val repoModule = module {
    single { ResourcesRepository(get()) }
}
