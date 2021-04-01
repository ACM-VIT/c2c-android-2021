package com.acmvit.c2c2021.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {
    private val isPending = AtomicBoolean(false)
    @MainThread
    override fun setValue(value: T) {
        isPending.compareAndSet(false, true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        // Observe the internal MutableLiveData
        super.observe(owner) { t: T ->
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}