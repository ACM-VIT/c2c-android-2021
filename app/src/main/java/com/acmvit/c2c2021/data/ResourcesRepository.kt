package com.acmvit.c2c2021.data

import com.acmvit.c2c2021.models.Timings
import com.acmvit.c2c2021.models.Track
import com.acmvit.c2c2021.util.Resource
import com.google.firebase.database.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.*
import java.util.concurrent.atomic.LongAccumulator

class ResourcesRepository(
    private val rtd: FirebaseRTD
) {
    fun getTimings() = rtd.getStreamAt(FirebaseRTD.TIMINGS) { it.getValue(Timings::class.java) }
        .observeOn(AndroidSchedulers.mainThread())

    fun getTracks() = rtd.getCachedValueAt(FirebaseRTD.TRACKS) {
        val listData = mutableListOf<Track>()
        for (snap: DataSnapshot in it.children) {
            snap.getValue(Track::class.java)?.let { it1 -> listData.add(it1) }
        }
        return@getCachedValueAt listData
    }.observeOn(AndroidSchedulers.mainThread())

    fun getCurrentTimeDiff(): Observable<Resource<Long>> = Observable.just(Resource.success(0.toLong()))
        .observeOn(AndroidSchedulers.mainThread())

}