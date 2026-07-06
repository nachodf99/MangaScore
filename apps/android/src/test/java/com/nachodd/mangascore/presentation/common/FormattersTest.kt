package com.nachodd.mangascore.presentation.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.TimeZone

class FormattersTest {
    @Test
    fun `format weight keeps grams under one kilogram`() {
        assertEquals("950 g", formatWeightGrams(950))
    }

    @Test
    fun `format weight uses kilograms with decimal comma`() {
        assertEquals("1,20 kg", formatWeightGrams(1200))
        assertEquals("2,35 kg", formatWeightGrams(2350))
    }

    @Test
    fun `format timestamp shows readable date and time`() {
        val defaultTimeZone = TimeZone.getDefault()
        try {
            TimeZone.setDefault(TimeZone.getTimeZone("Europe/Madrid"))

            assertEquals("06/07/2026 12:45", formatTimestamp(1_783_334_700_000L))
        } finally {
            TimeZone.setDefault(defaultTimeZone)
        }
    }
}
