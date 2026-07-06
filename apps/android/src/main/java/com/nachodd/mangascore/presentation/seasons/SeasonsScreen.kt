package com.nachodd.mangascore.presentation.seasons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachodd.mangascore.data.repository.LocalMangaScoreRepository
import com.nachodd.mangascore.domain.model.Season
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import com.nachodd.mangascore.presentation.common.formatTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SeasonsScreen {
    @Composable
    operator fun invoke(
        onSeasonClick: (String) -> Unit,
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: SeasonsViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        SeasonsContent(
            uiState = uiState,
            onSeasonClick = onSeasonClick,
            onBackClick = onBackClick,
            onCreateClick = viewModel::showCreateDialog,
            onDismissDialog = viewModel::hideCreateDialog,
            onNameChange = viewModel::onNameChange,
            onStartTimestampChange = viewModel::onStartTimestampChange,
            onDiscardWorstRoundsChange = viewModel::onDiscardWorstRoundsChange,
            onSaveSeason = viewModel::saveSeason,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonsContent(
    uiState: SeasonsUiState,
    onSeasonClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onNameChange: (String) -> Unit,
    onStartTimestampChange: (String) -> Unit,
    onDiscardWorstRoundsChange: (String) -> Unit,
    onSaveSeason: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Temporadas") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Text("+")
            }
        },
    ) { paddingValues ->
        if (uiState.seasons.isEmpty()) {
            EmptyStateContent(
                title = "Sin temporadas",
                description = "Crea la primera temporada del club para empezar a organizar mangas y clasificaciones.",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(uiState.seasons, key = { it.id }) { season ->
                    SeasonCard(
                        season = season,
                        onClick = { onSeasonClick(season.id) },
                    )
                }
            }
        }
    }

    if (uiState.isCreateDialogVisible) {
        CreateSeasonDialog(
            uiState = uiState,
            onDismiss = onDismissDialog,
            onNameChange = onNameChange,
            onStartTimestampChange = onStartTimestampChange,
            onDiscardWorstRoundsChange = onDiscardWorstRoundsChange,
            onSave = onSaveSeason,
        )
    }
}

@Composable
private fun SeasonCard(
    season: Season,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = season.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Inicio: ${formatTimestamp(season.startsAt)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Descartes: ${season.discardWorstRounds}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CreateSeasonDialog(
    uiState: SeasonsUiState,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onStartTimestampChange: (String) -> Unit,
    onDiscardWorstRoundsChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear temporada") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = uiState.name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") },
                    singleLine = true,
                )
                TextField(
                    value = uiState.startTimestamp,
                    onValueChange = onStartTimestampChange,
                    label = { Text("Inicio opcional (timestamp)") },
                    singleLine = true,
                )
                TextField(
                    value = uiState.discardWorstRounds,
                    onValueChange = onDiscardWorstRoundsChange,
                    label = { Text("Descartes de peores mangas") },
                    singleLine = true,
                )
                uiState.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onSave) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

data class SeasonsUiState(
    val seasons: List<Season> = emptyList(),
    val isCreateDialogVisible: Boolean = false,
    val name: String = "",
    val startTimestamp: String = "",
    val discardWorstRounds: String = "0",
    val errorMessage: String? = null,
)

@HiltViewModel
class SeasonsViewModel @Inject constructor(
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SeasonsUiState())
    val uiState: StateFlow<SeasonsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeSeasons().collect { seasons ->
                _uiState.update { it.copy(seasons = seasons) }
            }
        }
    }

    fun showCreateDialog() {
        _uiState.update {
            it.copy(
                isCreateDialogVisible = true,
                errorMessage = null,
            )
        }
    }

    fun hideCreateDialog() {
        _uiState.update {
            it.copy(
                isCreateDialogVisible = false,
                name = "",
                startTimestamp = "",
                discardWorstRounds = "0",
                errorMessage = null,
            )
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onStartTimestampChange(value: String) {
        _uiState.update { it.copy(startTimestamp = value, errorMessage = null) }
    }

    fun onDiscardWorstRoundsChange(value: String) {
        _uiState.update { it.copy(discardWorstRounds = value, errorMessage = null) }
    }

    fun saveSeason() {
        val state = _uiState.value
        val name = state.name.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es obligatorio.") }
            return
        }

        val startTimestampInput = state.startTimestamp.trim()
        val startsAt = if (startTimestampInput.isBlank()) {
            System.currentTimeMillis()
        } else {
            startTimestampInput.toLongOrNull()
        }
        if (startsAt == null) {
            _uiState.update { it.copy(errorMessage = "El inicio debe ser un timestamp valido.") }
            return
        }
        val discardWorstRounds = state.discardWorstRounds.trim().toIntOrNull()
        if (discardWorstRounds == null || discardWorstRounds < 0) {
            _uiState.update { it.copy(errorMessage = "Los descartes deben ser un numero valido.") }
            return
        }

        viewModelScope.launch {
            repository.createSeason(
                name = name,
                startsAt = startsAt,
                discardWorstRounds = discardWorstRounds,
            )
            hideCreateDialog()
        }
    }
}
