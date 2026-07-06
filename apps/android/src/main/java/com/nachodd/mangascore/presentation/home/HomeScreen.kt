package com.nachodd.mangascore.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nachodd.mangascore.presentation.theme.MangaScoreTheme

object HomeScreen {
    @Composable
    operator fun invoke(
        onSeasonsClick: () -> Unit,
        onTournamentsClick: () -> Unit,
        onParticipantsClick: () -> Unit,
        onSyncClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val accessItems = listOf(
            HomeAccessItem(
                title = "Temporadas",
                description = "Liga, mangas y clasificación general del club.",
                actionLabel = "Ver temporadas",
                onClick = onSeasonsClick,
            ),
            HomeAccessItem(
                title = "Torneos de un día",
                description = "Eventos independientes con ranking propio.",
                actionLabel = "Ver torneos",
                onClick = onTournamentsClick,
            ),
            HomeAccessItem(
                title = "Participantes",
                description = "Pescadores, inscripciones y datos del club.",
                actionLabel = "Ver participantes",
                onClick = onParticipantsClick,
            ),
            HomeAccessItem(
                title = "Sincronización",
                description = "Estado offline y datos pendientes de enviar.",
                actionLabel = "Ver estado",
                onClick = onSyncClick,
            ),
        )

        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                HomeHeader()
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    accessItems.forEachIndexed { index, item ->
                        HomeAccessCard(
                            item = item,
                            index = index + 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "MangaScore",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "Gestiona la actividad del club desde una base offline preparada para temporadas, torneos, capturas y rankings.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}

@Composable
private fun HomeAccessCard(
    item: HomeAccessItem,
    index: Int,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = item.onClick,
        modifier = modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            NumberBadge(index = index)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(2.dp))
                TextButton(
                    onClick = item.onClick,
                    contentPadding = PaddingValues(horizontal = 0.dp),
                ) {
                    Text(text = item.actionLabel)
                }
            }
        }
    }
}

@Composable
private fun NumberBadge(index: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 9.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = index.toString(),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

private data class HomeAccessItem(
    val title: String,
    val description: String,
    val actionLabel: String,
    val onClick: () -> Unit,
)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MangaScoreTheme {
        HomeScreen(
            onSeasonsClick = {},
            onTournamentsClick = {},
            onParticipantsClick = {},
            onSyncClick = {},
        )
    }
}
