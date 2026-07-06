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
import androidx.compose.material3.Button
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
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachodd.mangascore.data.repository.LocalMangaScoreRepository
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.model.Season
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object SeasonDetailScreen {
    @Composable
    operator fun invoke(
        onSeasonRankingClick: (String) -> Unit,
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: SeasonDetailViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        SeasonDetailContent(
            uiState = uiState,
            onSeasonRankingClick = onSeasonRankingClick,
            onBackClick = onBackClick,
            onCreateRoundClick = viewModel::showCreateDialog,
            onDismissDialog = viewModel::hideCreateDialog,
            onRoundNameChange = viewModel::onRoundNameChange,
            onRoundNumberChange = viewModel::onRoundNumberChange,
            onRoundLocationChange = viewModel::onRoundLocationChange,
            onSaveRound = viewModel::saveRound,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonDetailContent(
    uiState: SeasonDetailUiState,
    onSeasonRankingClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateRoundClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onRoundNameChange: (String) -> Unit,
    onRoundNumberChange: (String) -> Unit,
    onRoundLocationChange: (String) -> Unit,
    onSaveRound: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(uiState.season?.name ?: "Temporada") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateRoundClick) {
                Text("+")
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Button(
                onClick = { onSeasonRankingClick(uiState.seasonId) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Text("Clasificacion general")
            }

            if (uiState.rounds.isEmpty()) {
                EmptyStateContent(
                    title = "Sin mangas",
                    description = "Crea la primera manga de esta temporada para empezar a registrar jornadas.",
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(uiState.rounds, key = { it.id }) { round ->
                        RoundCard(round = round)
                    }
                }
            }
        }
    }

    if (uiState.isCreateDialogVisible) {
        CreateRoundDialog(
            uiState = uiState,
            onDismiss = onDismissDialog,
            onRoundNameChange = onRoundNameChange,
            onRoundNumberChange = onRoundNumberChange,
            onRoundLocationChange = onRoundLocationChange,
            onSave = onSaveRound,
        )
    }
}

@Composable
private fun RoundCard(
    round: Round,
    modifier: Modifier = Modifier,
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = round.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Programada: ${round.scheduledAt}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CreateRoundDialog(
    uiState: SeasonDetailUiState,
    onDismiss: () -> Unit,
    onRoundNameChange: (String) -> Unit,
    onRoundNumberChange: (String) -> Unit,
    onRoundLocationChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear manga") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = uiState.roundName,
                    onValueChange = onRoundNameChange,
                    label = { Text("Nombre") },
                    singleLine = true,
                )
                TextField(
                    value = uiState.roundNumber,
                    onValueChange = onRoundNumberChange,
                    label = { Text("Numero de manga") },
                    singleLine = true,
                )
                TextField(
                    value = uiState.roundLocation,
                    onValueChange = onRoundLocationChange,
                    label = { Text("Lugar opcional") },
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

data class SeasonDetailUiState(
    val seasonId: String = "",
    val season: Season? = null,
    val rounds: List<Round> = emptyList(),
    val isCreateDialogVisible: Boolean = false,
    val roundName: String = "",
    val roundNumber: String = "",
    val roundLocation: String = "",
    val errorMessage: String? = null,
)

@HiltViewModel
class SeasonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val seasonId: String = checkNotNull(savedStateHandle[MangaScoreRoute.SeasonDetail.SEASON_ID_ARG])

    private val _uiState = MutableStateFlow(SeasonDetailUiState(seasonId = seasonId))
    val uiState: StateFlow<SeasonDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(season = repository.getSeason(seasonId)) }
        }
        viewModelScope.launch {
            repository.observeRounds(seasonId).collect { rounds ->
                _uiState.update { it.copy(rounds = rounds) }
            }
        }
    }

    fun showCreateDialog() {
        _uiState.update { it.copy(isCreateDialogVisible = true, errorMessage = null) }
    }

    fun hideCreateDialog() {
        _uiState.update {
            it.copy(
                isCreateDialogVisible = false,
                roundName = "",
                roundNumber = "",
                roundLocation = "",
                errorMessage = null,
            )
        }
    }

    fun onRoundNameChange(value: String) {
        _uiState.update { it.copy(roundName = value, errorMessage = null) }
    }

    fun onRoundNumberChange(value: String) {
        _uiState.update { it.copy(roundNumber = value, errorMessage = null) }
    }

    fun onRoundLocationChange(value: String) {
        _uiState.update { it.copy(roundLocation = value, errorMessage = null) }
    }

    fun saveRound() {
        val state = _uiState.value
        val name = state.roundName.trim()
        val number = state.roundNumber.trim().toIntOrNull()

        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es obligatorio.") }
            return
        }
        if (number == null || number <= 0) {
            _uiState.update { it.copy(errorMessage = "El numero de manga debe ser valido.") }
            return
        }

        viewModelScope.launch {
            repository.createRound(
                seasonId = seasonId,
                name = name,
                roundNumber = number,
                location = state.roundLocation,
            )
            hideCreateDialog()
        }
    }
}
