package com.nachodd.mangascore.presentation.participants

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
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object ParticipantsScreen {
    @Composable
    operator fun invoke(
        onParticipantClick: (String) -> Unit,
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: ParticipantsViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        ParticipantsContent(
            uiState = uiState,
            onParticipantClick = onParticipantClick,
            onBackClick = onBackClick,
            onCreateClick = viewModel::showCreateDialog,
            onDismissDialog = viewModel::hideCreateDialog,
            onFullNameChange = viewModel::onFullNameChange,
            onAliasChange = viewModel::onAliasChange,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            onDniChange = viewModel::onDniChange,
            onNirChange = viewModel::onNirChange,
            onNiraChange = viewModel::onNiraChange,
            onSaveParticipant = viewModel::saveParticipant,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ParticipantsContent(
    uiState: ParticipantsUiState,
    onParticipantClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onAliasChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onNirChange: (String) -> Unit,
    onNiraChange: (String) -> Unit,
    onSaveParticipant: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Participantes") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Text("+")
            }
        },
    ) { paddingValues ->
        if (uiState.participants.isEmpty()) {
            EmptyStateContent(
                title = "Sin participantes",
                description = "Agrega pescadores del club para usarlos despues en temporadas, mangas y torneos.",
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
                items(uiState.participants, key = { it.id }) { participant ->
                    ParticipantCard(
                        participant = participant,
                        onClick = { onParticipantClick(participant.id) },
                    )
                }
            }
        }
    }

    if (uiState.isCreateDialogVisible) {
        CreateParticipantDialog(
            uiState = uiState,
            onDismiss = onDismissDialog,
            onFullNameChange = onFullNameChange,
            onAliasChange = onAliasChange,
            onPhoneNumberChange = onPhoneNumberChange,
            onDniChange = onDniChange,
            onNirChange = onNirChange,
            onNiraChange = onNiraChange,
            onSave = onSaveParticipant,
        )
    }
}

@Composable
private fun ParticipantCard(
    participant: Participant,
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
                text = participant.fullName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = "NIR: ${participant.nir.orNotIndicated()} - Tel: ${participant.phoneNumber.orNotIndicated()}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = "Toca para ver ficha",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun CreateParticipantDialog(
    uiState: ParticipantsUiState,
    onDismiss: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onAliasChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDniChange: (String) -> Unit,
    onNirChange: (String) -> Unit,
    onNiraChange: (String) -> Unit,
    onSave: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar participante") },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    TextField(
                        value = uiState.fullName,
                        onValueChange = onFullNameChange,
                        label = { Text("Nombre completo") },
                        singleLine = true,
                    )
                }
                item {
                    TextField(
                        value = uiState.alias,
                        onValueChange = onAliasChange,
                        label = { Text("Alias opcional") },
                        singleLine = true,
                    )
                }
                item {
                    TextField(
                        value = uiState.phoneNumber,
                        onValueChange = onPhoneNumberChange,
                        label = { Text("Telefono opcional") },
                        singleLine = true,
                    )
                }
                item {
                    TextField(
                        value = uiState.dni,
                        onValueChange = onDniChange,
                        label = { Text("DNI opcional") },
                        singleLine = true,
                    )
                }
                item {
                    TextField(
                        value = uiState.nir,
                        onValueChange = onNirChange,
                        label = { Text("NIR opcional") },
                        singleLine = true,
                    )
                }
                item {
                    TextField(
                        value = uiState.nira,
                        onValueChange = onNiraChange,
                        label = { Text("NIRA opcional") },
                        singleLine = true,
                    )
                }
                uiState.errorMessage?.let { error ->
                    item {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
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

data class ParticipantsUiState(
    val participants: List<Participant> = emptyList(),
    val isCreateDialogVisible: Boolean = false,
    val fullName: String = "",
    val alias: String = "",
    val phoneNumber: String = "",
    val dni: String = "",
    val nir: String = "",
    val nira: String = "",
    val errorMessage: String? = null,
)

@HiltViewModel
class ParticipantsViewModel @Inject constructor(
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ParticipantsUiState())
    val uiState: StateFlow<ParticipantsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeParticipants().collect { participants ->
                _uiState.update { it.copy(participants = participants) }
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
                fullName = "",
                alias = "",
                phoneNumber = "",
                dni = "",
                nir = "",
                nira = "",
                errorMessage = null,
            )
        }
    }

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, errorMessage = null) }
    }

    fun onAliasChange(value: String) {
        _uiState.update { it.copy(alias = value, errorMessage = null) }
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.update { it.copy(phoneNumber = value, errorMessage = null) }
    }

    fun onDniChange(value: String) {
        _uiState.update { it.copy(dni = value, errorMessage = null) }
    }

    fun onNirChange(value: String) {
        _uiState.update { it.copy(nir = value, errorMessage = null) }
    }

    fun onNiraChange(value: String) {
        _uiState.update { it.copy(nira = value, errorMessage = null) }
    }

    fun saveParticipant() {
        val state = _uiState.value
        val fullName = state.fullName.trim()
        if (fullName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre completo es obligatorio.") }
            return
        }

        viewModelScope.launch {
            repository.createParticipant(
                fullName = fullName,
                alias = state.alias,
                phoneNumber = state.phoneNumber,
                dni = state.dni,
                nir = state.nir,
                nira = state.nira,
            )
            hideCreateDialog()
        }
    }
}

private fun String?.orNotIndicated(): String = this?.takeIf(String::isNotBlank) ?: "No indicado"
