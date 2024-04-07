package com.example.moengageassignment.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material.icons.sharp.KeyboardArrowUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.moengageassignment.data.dto.NewsArticle
import com.example.moengageassignment.ui.components.DropDownMenuType
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

    fun NewsArticle.toNewsFooter(): String {
        val formattedTime = Utility.convertTimestampToFormattedDate(publishedAt.toString())
        return "$formattedTime • $author"
    }

    fun NewsArticle.openNewsInBrowser(context: Context) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun DropDownMenuType.toTitle(): String {
        return if (this == DropDownMenuType.OldToNew) {
            "Old To New"

        } else {
            "New To Old"
        }
    }

    fun DropDownMenuType.toLeadingIcon(): ImageVector {
        return if (this == DropDownMenuType.OldToNew) {
            Icons.Sharp.KeyboardArrowDown

        } else {
            Icons.Sharp.KeyboardArrowUp
        }
    }
}