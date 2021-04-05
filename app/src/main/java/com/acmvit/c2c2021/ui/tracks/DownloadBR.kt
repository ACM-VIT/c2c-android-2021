package com.acmvit.c2c2021.ui.tracks

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class DownloadBR : BroadcastReceiver() {
    private val TAG = "DownloadBR"
    private val _downloadCompleteObservable = BehaviorSubject.create<Long>()
    val downloadCompleteObservable: Observable<Long>
        get() = _downloadCompleteObservable

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)?.let {
            Log.d(TAG, "onReceive: ")
            _downloadCompleteObservable.onNext(it)
        }
    }

}