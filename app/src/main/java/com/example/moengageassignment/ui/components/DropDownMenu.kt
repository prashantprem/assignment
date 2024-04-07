package com.example.moengageassignment.ui.components

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.moengageassignment.utils.Extensions.toLeadingIcon
import com.example.moengageassignment.utils.Extensions.toTitle

@Composable
fun AppDropDownMenu(
    onClick: (DropDownMenuType) -> Unit,
    expanded: Boolean,
    onDismissRequest: () -> Unit
) {
    val items = listOf(
        DropDownMenuType.NewToOld,
        DropDownMenuType.OldToNew
    )
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest() },
        modifier = Modifier.background(color = Color(0xffffc400))
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.toTitle(), style = MaterialTheme.typography.labelMedium) },
                onClick = { onClick(item) },
                trailingIcon = {
                    Icon(
                        imageVector = item.toLeadingIcon(),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            )
        }
    }
}


enum class DropDownMenuType {
    NewToOld, OldToNew
}
