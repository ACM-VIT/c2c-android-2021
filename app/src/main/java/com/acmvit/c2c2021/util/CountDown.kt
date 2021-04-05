package com.acmvit.c2c2021.util

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.TimeUnit

class CountDown(
    private var timeRem: Long
) {
    private val TAG = "CountDown"
    private val subject: BehaviorSubject<List<Long>> = BehaviorSubject.create()
    private var disposable: Disposable? = null

    init {
        reset(timeRem)
    }

    fun reset(fromms: Long) {
        timeRem = fromms
        disposable?.dispose()
        disposable = Observable
            .interval(1, 1, TimeUnit.SECONDS)
            .subscribe{
                Log.d(TAG, "reset: $it")
                timeRem -= 1000
                if (timeRem < 0) {
                    disposable?.dispose()
                    timeRem = 0
                }
                subject.onNext(getAccTimeDiff(timeRem))
            }
    }

    fun stop() {
        subject.onComplete()
        disposable?.dispose()
    }

    fun asStringObservable() = subject.map { it.map { String.format("%2d", it) } }.observeOn(AndroidSchedulers.mainThread())
    fun asLongObservable(): Observable<List<Long>> = subject.observeOn(AndroidSchedulers.mainThread())

    private fun getAccTimeDiff(initDiff: Long): List<Long> {
        var diff = initDiff
        val timeDiff = mutableListOf<Long>()

        for (unit in arrayOf(TimeUnit.HOURS, TimeUnit.MINUTES, TimeUnit.SECONDS)) {
            unit.convert(diff, TimeUnit.MILLISECONDS).let {
                timeDiff.add(it)
                diff -= TimeUnit.MILLISECONDS.convert(it, unit)
            }
        }

        return timeDiff
    }

    fun getCorrespondingColor(colorMap: Map<Long, Int>): Int? {
        val timeValues = colorMap.keys.toTypedArray().sortedArrayDescending()

        for (timeVal in timeValues) {
            if (timeRem >= timeVal) {
                return colorMap[timeVal]
            }
        }

        return colorMap[timeValues.last()]
    }

}