package com.example.moengageassignment.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.android.gms.common.api.ApiException
import java.io.IOException
import java.io.InputStream
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
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

    fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getErrorMessage(exception: Throwable): String {
        return when (exception) {
            is ApiException -> {
                return when (exception.statusCode) {
                    401 -> "Unauthorized access. Please log in again."
                    404 -> "Resource not found"
                    500 -> "Internal Server Error"
                    else -> "An error occurred. Please try again later"

                }
            }

            is IOException -> {
                when (exception) {
                    is ConnectException -> "No internet connection. Please check your connection"
                    is UnknownHostException -> "No internet connection. Please check your connection"
                    else -> "Network error occurred. Please try again"
                }
            }

            else -> {
                "An error occurred. Please try again later"
            }
        }
    }

}