package com.nachodd.mangascore.presentation.participants

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nachodd.mangascore.presentation.common.FeaturePlaceholderScreen

object ParticipantsScreen {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        FeaturePlaceholderScreen(
            title = "Participantes",
            description = "Aquí se administrarán pescadores del club y sus futuras inscripciones en mangas y torneos.",
            modifier = modifier,
        )
    }
}
