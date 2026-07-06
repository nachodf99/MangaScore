package com.nachodd.mangascore.presentation.sync

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nachodd.mangascore.presentation.common.FeaturePlaceholderScreen

object SyncStatusScreen {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        FeaturePlaceholderScreen(
            title = "Sincronización",
            description = "Aquí se mostrará el estado offline y los datos pendientes de sincronizar cuando se implemente la nube.",
            modifier = modifier,
        )
    }
}
