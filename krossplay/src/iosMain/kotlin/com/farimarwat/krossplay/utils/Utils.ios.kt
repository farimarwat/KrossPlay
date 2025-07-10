package com.farimarwat.krossplay.utils

actual fun Long.formatAsTime(): String {
    val totalSeconds = this / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    fun Long.padTwoDigits() = this.toString().padStart(2, '0')

    return if (hours > 0) {
        "${hours.padTwoDigits()}:${minutes.padTwoDigits()}:${seconds.padTwoDigits()}"
    } else {
        "${minutes.padTwoDigits()}:${seconds.padTwoDigits()}"
    }
}