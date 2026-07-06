package com.nachodd.mangascore.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nachodd.mangascore.presentation.theme.MangaScoreTheme

object HomeScreen {
    @Composable
    operator fun invoke(
        onSeasonsClick: () -> Unit,
        onOneDayTournamentsClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "MangaScore",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Gestiona temporadas, mangas quincenales y torneos de pesca desde una base preparada para crecer.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                HomeAccessCard(
                    title = "Temporadas",
                    description = "Organiza temporadas, mangas y clasificaciones generales.",
                    onClick = onSeasonsClick,
                )
                HomeAccessCard(
                    title = "Torneos de un día",
                    description = "Accede a la gestión de torneos puntuales del club.",
                    onClick = onOneDayTournamentsClick,
                )
            }
        }
    }

    @Composable
    fun Placeholder(title: String, modifier: Modifier = Modifier) {
        Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
            ) {
                Text(text = title, style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Pantalla preparada para próximos issues.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun HomeAccessCard(
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            TextButton(
                onClick = onClick,
                contentPadding = PaddingValues(horizontal = 0.dp),
            ) {
                Text(text = "Entrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MangaScoreTheme {
        HomeScreen(onSeasonsClick = {}, onOneDayTournamentsClick = {})
    }
}
