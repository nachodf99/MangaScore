package com.nachodd.mangascore.presentation.seasons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nachodd.mangascore.presentation.common.FeaturePlaceholderScreen

object SeasonsScreen {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        FeaturePlaceholderScreen(
            title = "Temporadas",
            description = "Aquí se gestionarán ligas, mangas quincenales y clasificaciones generales de temporada.",
            modifier = modifier,
        )
    }
}
