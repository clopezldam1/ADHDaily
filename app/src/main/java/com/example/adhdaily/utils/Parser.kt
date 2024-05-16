package com.example.adhdaily.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Parser {

    /**
     * Convertir valor String en LocalDateTime
     */
    fun parseStringToLocalDateTime(value: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        return LocalDateTime.parse(value, formatter)
    }
}