package com.example.moengageassignment.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Extensions {

    fun Long.toFormattedDate(): String {
        val date = Date(this)
        val pattern = "EEEE, MMMM d"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        val formattedDate = formatter.format(date)

        val day = SimpleDateFormat("d", Locale.getDefault()).format(date)
        val suffix = when {
            day.endsWith("1") && day != "11" -> "st"
            day.endsWith("2") && day != "12" -> "nd"
            day.endsWith("3") && day != "13" -> "rd"
            else -> "th"
        }
        return "$formattedDate$suffix"
    }
}