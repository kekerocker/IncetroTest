package com.dsoft.domain.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

fun timestampToLocalDateTime(timestamp: Float): LocalDateTime {
    val milliseconds = timestamp * 1000
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds.toLong()), ZoneId.systemDefault())
}

fun secondsToLocalTime(seconds: Long): LocalTime = LocalTime.ofSecondOfDay(seconds)
