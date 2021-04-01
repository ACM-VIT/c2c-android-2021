package com.acmvit.c2c2021.util

import android.content.Context
import android.graphics.Color
import com.acmvit.c2c2021.R
import java.util.Calendar.MILLISECOND
import java.util.concurrent.TimeUnit

const val EXTERNAL_STORAGE = "C2C_2021"

val Context.COUNTDOWN_COLOR_MAP
    get() = mapOf(
        Pair(
            TimeUnit.MILLISECONDS.convert(6, TimeUnit.HOURS),
            getColor(R.color.colorGreen)
        ),
        Pair(
            TimeUnit.MILLISECONDS.convert(3, TimeUnit.HOURS),
            getColor(R.color.colorOrange)
        ),
        Pair(
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS),
            getColor(
                R.color.colorRed
            )
        )
    )

class NetworkException: Exception("Unable to connect to the internet")


