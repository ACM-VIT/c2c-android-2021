package com.acmvit.c2c2021.model

data class Timings(
    var eventStart: Long? = null,
    var submissionStart: Long? = null,
    var submissionEnd: Long? = null
) {
    fun convertToSecs() {
        eventStart = eventStart?.times(1000)
        submissionStart = submissionStart?.times(1000)
        submissionEnd = submissionEnd?.times(1000)
    }
}
