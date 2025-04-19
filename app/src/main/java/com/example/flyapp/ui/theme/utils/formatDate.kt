package com.example.flyapp.ui.theme.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(milliseconds: Long): String {
    val date = Date(milliseconds)
    val format = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return format.format(date)
}
