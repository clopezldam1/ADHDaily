package com.example.adhdaily.utils

import java.text.DateFormat

class Parser {
    /*
    fun parseDateTimeValue(value: String): Long {
        val edited = value.replace("T", "").replace("Z", "").replace("-", "")
        return if (edited.length == 14) {
            parseLongFormat(edited, value.endsWith("Z"))
        } else {
            val dateTimeFormat = DateFormat.forPattern("yyyyMMdd").withZoneUTC()
            val dateTime = dateTimeFormat.parseDateTime(edited)
            Formatter.getShiftedTS(dateTime = dateTime, toZone = DateTimeZone.getDefault())
        }
    }

    private fun parseLongFormat(digitString: String, useUTC: Boolean): Long {
        val dateTimeFormat = DateTimeFormat.forPattern("yyyyMMddHHmmss")
        val dateTimeZone = if (useUTC) DateTimeZone.UTC else DateTimeZone.getDefault()
        return dateTimeFormat.parseDateTime(digitString).withZoneRetainFields(dateTimeZone).seconds()
    }
    */
}