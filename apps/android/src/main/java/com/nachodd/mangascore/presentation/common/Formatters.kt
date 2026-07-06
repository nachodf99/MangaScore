package com.nachodd.mangascore.presentation.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val spanishLocale = Locale("es", "ES")

fun formatWeightGrams(weightGrams: Int): String =
    if (weightGrams < 1000) {
        "$weightGrams g"
    } else {
        String.format(spanishLocale, "%.2f kg", weightGrams / 1000.0)
    }

fun formatTimestamp(timestamp: Long): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm", spanishLocale).format(Date(timestamp))
