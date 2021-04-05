package com.acmvit.c2c2021.repository

import android.util.Log
import com.acmvit.c2c2021.C2CApp
import com.acmvit.c2c2021.isConnected
import com.acmvit.c2c2021.model.Timings
import com.acmvit.c2c2021.model.Track
import com.acmvit.c2c2021.util.NetworkException
import com.acmvit.c2c2021.util.Resource
import com.google.firebase.database.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.util.*

class ResourcesRepository(
    private val rtd: FirebaseRTD,
    private val cache: Cache
) {
    fun getTimings() = networkCheckedRun {
        rtd.getStreamAt(FirebaseRTD.TIMINGS) { it.getValue(Timings::class.java) }
            .map { res ->
                res.data?.let {
                    it.convertToSecs()
                    cache.timingsCache = it
                }; res
            }.startWith(Observable.just(Resource.loading(cache.timingsCache)))
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTracks() = networkCheckedRun {
        rtd.getCachedValueAt(FirebaseRTD.TRACKS) {
            val listData = mutableListOf<Track>()
            for (snap: DataSnapshot in it.children) {
                snap.getValue(Track::class.java)?.let { it1 -> listData.add(it1) }
            }
            return@getCachedValueAt listData
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun getCurrentTimeDiff() = networkCheckedRun {
        rtd.getCachedValueAt(FirebaseRTD.CURRENT_TIME_DIFF) { it.getValue(Long::class.java) }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun cacheEventStartState(hasStarted: Boolean) { cache.hasEventStarted = hasStarted }

    fun getCachedEventStartState() = cache.hasEventStarted

    private fun <T> networkCheckedRun(todo: () -> Observable<T>): Observable<T> {
        return if (!isConnected) Observable.error(NetworkException()) else todo()
    }
}
