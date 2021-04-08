package com.acmvit.c2c2021.util

import android.content.Context
import android.graphics.Color
import com.acmvit.c2c2021.R
import java.util.Calendar.MILLISECOND
import java.util.concurrent.TimeUnit

const val APP_EXTERNAL_STORAGE = "C2C 2021"
const val MIN_SNACKBAR_OFFSET = 30_000L

val Context.COUNTDOWN_COLOR_MAP
    get() = mapOf(
        Pair(
            TimeUnit.MILLISECONDS.convert(90, TimeUnit.MINUTES),
            getColor(R.color.colorGreen)
        ),
        Pair(
            TimeUnit.MILLISECONDS.convert(45, TimeUnit.MINUTES),
            getColor(R.color.colorOrange)
        ),
        Pair(
            TimeUnit.MILLISECONDS.convert(15, TimeUnit.MINUTES),
            getColor(
                R.color.colorRed
            )
        )
    )

class NetworkException: Exception("Unable to connect to the internet")


