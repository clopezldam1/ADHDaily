package com.example.adhdaily.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTimeToDate(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun dateToLocalDateTime(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromTimestampLongToDate(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestampLong(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromLongToCalendar(l: Long?): Calendar? {
        val c = Calendar.getInstance()
        c.timeInMillis = l!!
        return c
    }

    @TypeConverter
    fun fromCalendarToLong(c: Calendar?): Long? {
        return c?.time?.time
    }

}