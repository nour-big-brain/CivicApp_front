package com.example.civicapp.ui.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatDateTime(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun formatDate(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun formatTime(millis: Long): String {
        val date = Date(millis)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }

    fun Long.toFormattedDateTime(): String = DateTimeUtils.formatDateTime(this)
    fun Long.toFormattedDate(): String = DateTimeUtils.formatDate(this)
    fun Long.toFormattedTime(): String = DateTimeUtils.formatTime(this)

}
