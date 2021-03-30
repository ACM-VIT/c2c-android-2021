package com.acmvit.c2c2021.model

data class TimelineItem(
    val endUnix: Long = 0,
    val startUnix: Long = 0,
    val subtitle: String = "",
    val title: String = "",
)
