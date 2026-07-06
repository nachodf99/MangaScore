package com.nachodd.mangascore.presentation.tournaments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nachodd.mangascore.presentation.common.FeaturePlaceholderScreen

object TournamentsScreen {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        FeaturePlaceholderScreen(
            title = "Torneos de un día",
            description = "Aquí se prepararán torneos independientes con capturas, ranking y mayor pieza propios.",
            modifier = modifier,
        )
    }
}
