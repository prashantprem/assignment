package com.example.moengageassignment.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object Utility {

    fun convertTimestampToFormattedDate(timestamp: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val date = dateFormat.parse(timestamp)
            val formattedDate =
                SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(date)
            formattedDate
        } catch (e: Exception) {
            "Invalid Timestamp"
        }
    }

}