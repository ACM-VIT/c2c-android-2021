package com.acmvit.c2c2021.repository

import android.annotation.SuppressLint
import android.content.Context
import com.acmvit.c2c2021.model.Timings
import com.google.gson.Gson

class Cache (
    context: Context,
    private val gson: Gson
) {
    private val pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private val editor = pref.edit()
    var timingsCache: Timings?
        get() = gson.fromJson(pref.getString(TIMINGS_CACHE, null), Timings::class.java)
        set(timings) {
            editor.putString(TIMINGS_CACHE, gson.toJson(timings))
            editor.apply()
        }

    var hasEventStarted: Boolean
        get() = pref.getBoolean(EVENT_STARTED_CACHE, false)
        set(hasStarted) {
            editor.putBoolean(EVENT_STARTED_CACHE, hasStarted)
            editor.apply()
        }

    fun clearCache() {
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREF_NAME = "BaseCache"
        private const val TIMINGS_CACHE = "TimingsCache"
        private const val EVENT_STARTED_CACHE = "EventStarted"
    }

}