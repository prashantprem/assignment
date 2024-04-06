package com.example.moengageassignment.data.client

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkClient {
    fun fetchDataFromUrl(urlString: String): Result<JSONObject> {
        var jsonResponse: JSONObject? = null
        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                val response = StringBuilder()
                bufferedReader.useLines { lines -> lines.forEach { response.append(it) } }
                jsonResponse = JSONObject(response.toString())
                connection.disconnect()
                return Result.success(jsonResponse)
            } else {
                connection.disconnect()
                return Result.failure(Error(connection.responseMessage))
                Log.e("NetworkClient", "Error: ${connection.responseMessage}")
            }

        } catch (e: Exception) {
            Log.e("NetworkClient", "Error fetching data", e)
            return Result.failure(e)
        }
    }
}
