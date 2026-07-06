package com.nachodd.mangascore.presentation.tournaments

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
import com.nachodd.mangascore.domain.model.Tournament
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object TournamentsScreen {
    @Composable
    operator fun invoke(
        onTournamentClick: (String) -> Unit,
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: TournamentsViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        TournamentsContent(
            uiState = uiState,
            onTournamentClick = onTournamentClick,
            onBackClick = onBackClick,
            onCreateClick = viewModel::showCreateDialog,
            onDismissDialog = viewModel::hideCreateDialog,
            onNameChange = viewModel::onNameChange,
            onLocationChange = viewModel::onLocationChange,
            onSaveTournament = viewModel::saveTournament,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TournamentsContent(
    uiState: TournamentsUiState,
    onTournamentClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSaveTournament: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Torneos de un dia") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Text("+")
            }
        },
    ) { paddingValues ->
        if (uiState.tournaments.isEmpty()) {
            EmptyStateContent(
                title = "Sin torneos",
                description = "Crea un torneo de un dia para preparar su ranking y capturas.",
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
                items(uiState.tournaments, key = { it.id }) { tournament ->
                    TournamentCard(
                        tournament = tournament,
                        onClick = { onTournamentClick(tournament.id) },
                    )
                }
            }
        }
    }

    if (uiState.isCreateDialogVisible) {
        CreateTournamentDialog(
            uiState = uiState,
            onDismiss = onDismissDialog,
            onNameChange = onNameChange,
            onLocationChange = onLocationChange,
            onSave = onSaveTournament,
        )
    }
}

@Composable
private fun TournamentCard(
    tournament: Tournament,
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
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = tournament.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "Fecha: ${tournament.scheduledAt}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun CreateTournamentDialog(
    uiState: TournamentsUiState,
    onDismiss: () -> Unit,
    onNameChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear torneo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = uiState.name,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") },
                    singleLine = true,
                )
                TextField(
                    value = uiState.location,
                    onValueChange = onLocationChange,
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

data class TournamentsUiState(
    val tournaments: List<Tournament> = emptyList(),
    val isCreateDialogVisible: Boolean = false,
    val name: String = "",
    val location: String = "",
    val errorMessage: String? = null,
)

@HiltViewModel
class TournamentsViewModel @Inject constructor(
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TournamentsUiState())
    val uiState: StateFlow<TournamentsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeTournaments().collect { tournaments ->
                _uiState.update { it.copy(tournaments = tournaments) }
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
                name = "",
                location = "",
                errorMessage = null,
            )
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(name = value, errorMessage = null) }
    }

    fun onLocationChange(value: String) {
        _uiState.update { it.copy(location = value, errorMessage = null) }
    }

    fun saveTournament() {
        val state = _uiState.value
        val name = state.name.trim()
        if (name.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es obligatorio.") }
            return
        }

        viewModelScope.launch {
            repository.createTournament(
                name = name,
                location = state.location,
            )
            hideCreateDialog()
        }
    }
}
