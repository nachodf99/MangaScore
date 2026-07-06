package com.nachodd.mangascore.presentation.rounds

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nachodd.mangascore.data.repository.LocalMangaScoreRepository
import com.nachodd.mangascore.domain.model.Catch
import com.nachodd.mangascore.domain.model.CompetitionType
import com.nachodd.mangascore.domain.model.Participant
import com.nachodd.mangascore.domain.model.RankingItem
import com.nachodd.mangascore.domain.model.Round
import com.nachodd.mangascore.domain.ranking.BiggestCatchCalculator
import com.nachodd.mangascore.domain.ranking.RankingCalculator
import com.nachodd.mangascore.presentation.catches.BiggestCatchCard
import com.nachodd.mangascore.presentation.catches.CatchList
import com.nachodd.mangascore.presentation.catches.CatchRegistrationDialog
import com.nachodd.mangascore.presentation.catches.RankingOverview
import com.nachodd.mangascore.presentation.common.BackNavigationButton
import com.nachodd.mangascore.presentation.common.EmptyStateContent
import com.nachodd.mangascore.presentation.navigation.MangaScoreRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object RoundDetailScreen {
    @Composable
    operator fun invoke(
        onBackClick: () -> Unit,
        modifier: Modifier = Modifier,
        viewModel: RoundDetailViewModel = hiltViewModel(),
    ) {
        val uiState by viewModel.uiState.collectAsState()

        RoundDetailContent(
            uiState = uiState,
            onBackClick = onBackClick,
            onRegisterCatchClick = viewModel::showRegisterCatchDialog,
            onDismissDialog = viewModel::hideRegisterCatchDialog,
            onParticipantSelected = viewModel::onParticipantSelected,
            onWeightGramsChange = viewModel::onWeightGramsChange,
            onSpeciesChange = viewModel::onSpeciesChange,
            onSaveCatch = viewModel::saveCatch,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoundDetailContent(
    uiState: RoundDetailUiState,
    onBackClick: () -> Unit,
    onRegisterCatchClick: () -> Unit,
    onDismissDialog: () -> Unit,
    onParticipantSelected: (String) -> Unit,
    onWeightGramsChange: (String) -> Unit,
    onSpeciesChange: (String) -> Unit,
    onSaveCatch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(uiState.round?.name ?: "Manga") },
                navigationIcon = { BackNavigationButton(onBackClick = onBackClick) },
            )
        },
    ) { paddingValues ->
        if (uiState.catches.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onRegisterCatchClick,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Registrar captura")
                }
                EmptyStateContent(
                    title = "Sin capturas",
                    description = "Registra la primera captura de esta manga para ver ranking, top 3 y mayor pieza.",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Button(
                        onClick = onRegisterCatchClick,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Registrar captura")
                    }
                }
                item {
                    BiggestCatchCard(
                        catch = uiState.biggestCatch,
                        participantNameById = uiState.participantNameById,
                    )
                }
                item {
                    RankingOverview(
                        ranking = uiState.ranking,
                        participantNameById = uiState.participantNameById,
                    )
                }
                item {
                    CatchList(
                        catches = uiState.catches,
                        participantNameById = uiState.participantNameById,
                    )
                }
            }
        }
    }

    if (uiState.isRegisterCatchDialogVisible) {
        CatchRegistrationDialog(
            participants = uiState.participants,
            selectedParticipantId = uiState.selectedParticipantId,
            weightGrams = uiState.weightGrams,
            species = uiState.species,
            errorMessage = uiState.errorMessage,
            onParticipantSelected = onParticipantSelected,
            onWeightGramsChange = onWeightGramsChange,
            onSpeciesChange = onSpeciesChange,
            onDismiss = onDismissDialog,
            onSave = onSaveCatch,
        )
    }
}

data class RoundDetailUiState(
    val roundId: String = "",
    val round: Round? = null,
    val catches: List<Catch> = emptyList(),
    val participants: List<Participant> = emptyList(),
    val participantNameById: Map<String, String> = emptyMap(),
    val ranking: List<RankingItem> = emptyList(),
    val biggestCatch: Catch? = null,
    val isRegisterCatchDialogVisible: Boolean = false,
    val selectedParticipantId: String? = null,
    val weightGrams: String = "",
    val species: String = "",
    val errorMessage: String? = null,
)

@HiltViewModel
class RoundDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LocalMangaScoreRepository,
) : ViewModel() {
    private val roundId: String = checkNotNull(savedStateHandle[MangaScoreRoute.RoundDetail.ROUND_ID_ARG])
    private val rankingCalculator = RankingCalculator()
    private val biggestCatchCalculator = BiggestCatchCalculator()

    private val _uiState = MutableStateFlow(RoundDetailUiState(roundId = roundId))
    val uiState: StateFlow<RoundDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(round = repository.getRound(roundId)) }
        }
        viewModelScope.launch {
            combine(
                repository.observeCatchesByRound(roundId),
                repository.observeParticipants(),
            ) { catches, participants ->
                val ranking = if (catches.isEmpty()) {
                    emptyList()
                } else {
                    rankingCalculator.calculateRanking(
                        catches = catches,
                        participants = participants,
                        competitionType = CompetitionType.ROUND,
                        roundId = roundId,
                    )
                }
                CatchSummary(
                    catches = catches,
                    participants = participants,
                    participantNameById = participants.associate { it.id to it.fullName },
                    ranking = ranking,
                    biggestCatch = biggestCatchCalculator.getBiggestCatchForRound(catches, roundId),
                )
            }.collect { summary ->
                _uiState.update {
                    it.copy(
                        catches = summary.catches,
                        participants = summary.participants,
                        participantNameById = summary.participantNameById,
                        ranking = summary.ranking,
                        biggestCatch = summary.biggestCatch,
                    )
                }
            }
        }
    }

    fun showRegisterCatchDialog() {
        _uiState.update {
            it.copy(
                isRegisterCatchDialogVisible = true,
                selectedParticipantId = it.selectedParticipantId ?: it.participants.firstOrNull()?.id,
                errorMessage = null,
            )
        }
    }

    fun hideRegisterCatchDialog() {
        _uiState.update {
            it.copy(
                isRegisterCatchDialogVisible = false,
                selectedParticipantId = null,
                weightGrams = "",
                species = "",
                errorMessage = null,
            )
        }
    }

    fun onParticipantSelected(participantId: String) {
        _uiState.update { it.copy(selectedParticipantId = participantId, errorMessage = null) }
    }

    fun onWeightGramsChange(value: String) {
        _uiState.update { it.copy(weightGrams = value, errorMessage = null) }
    }

    fun onSpeciesChange(value: String) {
        _uiState.update { it.copy(species = value, errorMessage = null) }
    }

    fun saveCatch() {
        val state = _uiState.value
        val participantId = state.selectedParticipantId
        val weightGrams = state.weightGrams.trim().toIntOrNull()

        if (participantId == null) {
            _uiState.update { it.copy(errorMessage = "Selecciona un participante.") }
            return
        }
        if (state.weightGrams.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El peso es obligatorio.") }
            return
        }
        if (weightGrams == null || weightGrams <= 0) {
            _uiState.update { it.copy(errorMessage = "El peso debe ser mayor que 0.") }
            return
        }

        viewModelScope.launch {
            repository.createCatchForRound(
                roundId = roundId,
                participantId = participantId,
                weightGrams = weightGrams,
                species = state.species,
            )
            hideRegisterCatchDialog()
        }
    }

    private data class CatchSummary(
        val catches: List<Catch>,
        val participants: List<Participant>,
        val participantNameById: Map<String, String>,
        val ranking: List<RankingItem>,
        val biggestCatch: Catch?,
    )
}
