package com.example.socialnetwork.common

import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    private val simpleDateFormat =
        SimpleDateFormat("MMM dd, HH:mm")

    fun format(date: Date):String {
        return simpleDateFormat.format(date)
    }
}