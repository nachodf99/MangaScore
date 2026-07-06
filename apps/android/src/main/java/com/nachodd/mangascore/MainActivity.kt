package com.nachodd.mangascore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.nachodd.mangascore.presentation.MangaScoreApp
import com.nachodd.mangascore.presentation.theme.MangaScoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MangaScoreTheme {
                MangaScoreApp()
            }
        }
    }
}
