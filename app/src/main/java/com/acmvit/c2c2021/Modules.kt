package com.acmvit.c2c2021

import com.acmvit.c2c2021.repository.FirebaseRTD
import com.acmvit.c2c2021.repository.ResourcesRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val appModule = module {
    single { Firebase.database.reference }
    single { FirebaseRTD(get()) }
}

val repoModule = module {
    single { ResourcesRepository(get()) }
}
